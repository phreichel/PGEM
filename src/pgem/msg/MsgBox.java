//*************************************************************************************************
package pgem.msg;
//*************************************************************************************************

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import pgem.X;

//*************************************************************************************************
public final class MsgBox {

	//=============================================================================================
	private final Queue<Msg> msgQueue = new ArrayDeque<>();
	//=============================================================================================

	//=============================================================================================
	private final Queue<Msg> msgCache = new ArrayDeque<>();
	private final Map<Class<? extends MsgData>, Queue<MsgData>> dataCaches = new HashMap<>();
	//=============================================================================================

	//=============================================================================================
	private final Map<MsgType, List<MsgHandler>> msgHandlers = new EnumMap<>(MsgType.class);
	//=============================================================================================

	//=============================================================================================
	public void plug(MsgType type, MsgHandler handler) {
		var list = msgHandlers.get(type);
		if (list == null) {
			list = new ArrayList<MsgHandler>();
			msgHandlers.put(type, list);
		}
		list.add(handler);
	}
	//=============================================================================================

	//=============================================================================================
	public void unplug(MsgHandler handler) {
		for (var type : msgHandlers.keySet()) {
			var list = msgHandlers.get(type);
			list.remove(handler);
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	public void unplug(MsgType type, MsgHandler handler) {
		var list = msgHandlers.get(type);
		if (list == null) return;
		list.remove(handler);
	}
	//=============================================================================================
	
	//=============================================================================================
	public Msg alloc(MsgType type) {

		Msg msg = null;
		synchronized (msgCache) {
			msg = msgCache.poll();
		}
		if (msg == null) msg = new Msg();
		
		msg.time = System.currentTimeMillis();
		msg.type = type;
		msg.data = null;
		
		var cls = type.dataClass;
		if (cls == null) return msg;

		synchronized (dataCaches) {
			var cache = dataCaches.get(cls);
			if (cache != null) {
				msg.data = cache.poll();
			}
		}

		if (msg.data != null) return msg;
		
		try {

			var constructor = cls.getConstructor();
			msg.data = constructor.newInstance();
			return msg;
			
		} catch (Exception e) {
			throw new X(e);
		}
		
	}
	//=============================================================================================

	//=============================================================================================
	public void post(Msg msg) {
		synchronized (msgQueue) {
			msgQueue.offer(msg);
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void update() {
		synchronized (msgQueue) {
			Msg msg = msgQueue.poll();
			while (msg != null) {
				dispatch(msg);
				msg = msgQueue.poll();
			}
		}
	}
	//=============================================================================================

	//=============================================================================================
	private void dispatch(Msg msg) {

		if (msg == null) return;

		var list = msgHandlers.get(msg.type);
		if (list != null) {
			for (var msgHandler : list) {
				msgHandler.handle(msg);
			}
		}
		
		free(msg);

	}
	//=============================================================================================

	//=============================================================================================
	private void free(Msg msg) {
		
		var cls = msg.type.dataClass;
		if (cls != null) {
			
			msg.data.clear();

			synchronized (dataCaches) {
				var cache = dataCaches.get(cls);
				if (cache == null) {
					cache = new ArrayDeque<>();
					dataCaches.put(cls, cache);
				}
				cache.offer(msg.data);
			}
			
		}
		
		msg.clear();
		synchronized (msgCache) {
			msgCache.offer(msg);
		}
		
	}
	//=============================================================================================
	
}
//*************************************************************************************************

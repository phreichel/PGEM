//*************************************************************************************************
package pgem.msg;
//*************************************************************************************************

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

//*************************************************************************************************
public class MsgHub {

	//=============================================================================================
	private final Map<MsgType, List<MsgHandler>> map = new EnumMap<>(MsgType.class);
	private final List<Msg> inbox = new ArrayList<>();
	private final Queue<Msg> outbox = new ArrayDeque<>();
	private final Queue<Msg> cache  = new ArrayDeque<>();
	//=============================================================================================
	
	//=============================================================================================
	public MsgHub link(MsgType type, MsgHandler msgHandler) {
		var list = getOrCreate(type);
		list.add(msgHandler);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public MsgHub unlink(MsgType type, MsgHandler msgHandler) {
		var list = map.get(type);
		if (list != null) list.remove(msgHandler);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public MsgHub unlinkAll(MsgHandler msgHandler) {
		for (var type : map.keySet()) {
			var list = map.get(type);
			list.remove(msgHandler);
		}
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public Msg allocate() {
		Msg msg = null;
		synchronized (cache) {
			msg = cache.poll();
		}
		if (msg == null) msg = new Msg();
		return msg;
	}
	//=============================================================================================

	//=============================================================================================
	public MsgHub post(Msg msg) {
		synchronized (inbox) {
			inbox.add(msg);
		}
		return this;
	}
	//=============================================================================================
	
	//=============================================================================================
	public void dispatch() {
		synchronized (inbox) {
			outbox.addAll(inbox);
			inbox.clear();
		}
		while (!outbox.isEmpty()) {
			var msg = outbox.poll();
			var handlers = map.get(msg.type);
			if (handlers != null) {
				for (int j=0; j<handlers.size(); j++) {
					var handler = handlers.get(j);
					handler.handle(msg);
				}
			}
			msg.clear();
			synchronized (cache) {
				cache.offer(msg);
			}
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	private List<MsgHandler> getOrCreate(MsgType type) {
		var list = map.get(type);
		if (list == null) {
			list = new ArrayList<>();
			map.put(type, list);
		}
		return list;
	}
	//=============================================================================================
	
}
//*************************************************************************************************

//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import java.util.EnumMap;
import java.util.Map;

import javax.vecmath.Color4f;

//*************************************************************************************************
public class Style {

	//=============================================================================================
	private Style parent = null;
	//=============================================================================================
	
	//=============================================================================================
	private final Map<StyleColor, Color4f> colors = new EnumMap<>(StyleColor.class);
	private final Map<StyleFont, Font> fonts = new EnumMap<>(StyleFont.class);
	private final Map<StyleIcon, Icon> icons = new EnumMap<>(StyleIcon.class);
	//=============================================================================================

	//=============================================================================================
	public Style() {

		put(StyleColor.BUTTON_BACKGROUND, 0, 0, .8f, 1);
		put(StyleColor.BUTTON_BORDER_LIGHT, .4f, .4f, 1, 1);
		put(StyleColor.BUTTON_BORDER_DARK, 0, 0, 1, 1);
		put(StyleColor.FRAME_BACKGROUND, .4f, 0, 0, .4f);
		put(StyleColor.FRAME_BORDER, 0, 0, 1, 1);
		put(StyleColor.FRAME_BORDER_ARMED, 0, 1, 1, 1);
	    put(StyleColor.FRAME_ICON_COLOR, 0, 1, 1, 1);
	    put(StyleColor.FRAME_SCALE_BACKGROUND, 0, 0, 1, 1);
	    put(StyleColor.FRAME_SCALE_BORDER, 0, 0, 1, 1);
	    put(StyleColor.FRAME_TITLE_BACKGROUND, 0, 0, .6f, 1);
	    put(StyleColor.FRAME_TITLE_BORDER, 0, 0, 1, 1);
	    put(StyleColor.FRAME_TITLE_COLOR, 0, 1, 1, 1);
	    put(StyleColor.FRAME_TITLE_COLOR, 0, 1, 1, 1);
	    put(StyleColor.IMAGE_COLOR, 1, 1, 1, 1);
	    put(StyleColor.LABEL_COLOR, 1, 1, 1, 1);
	    put(StyleColor.MENU_BACKGROUND, 0, 0, 1f, .6f);
	    put(StyleColor.MENU_BORDER, 0, 0, 1, 1);
	    put(StyleColor.MENU_ITEM_BACKGROUND, 0, 0, 0, 0);
	    put(StyleColor.MENU_ITEM_BACKGROUND_ARMED, 0, 0, 1, 1);
	    put(StyleColor.MENU_ITEM_BORDER, 0, 0, 0, 0);
	    put(StyleColor.MENU_ITEM_ICON_COLOR, 0, 1, 1, 1);
	    put(StyleColor.MENU_ITEM_LABEL_COLOR, 0, 1, 1, 1);
	    put(StyleColor.PANEL_BACKGROUND, 0, 0, .4f, .6f);
	    put(StyleColor.PANEL_BORDER, 0, 0, 1, 1);

		put(StyleFont.DEFAULT, Font.DEFAULT_16);
		put(StyleFont.LABEL, Font.DEFAULT_16);
		put(StyleFont.FRAME_TITLE, Font.DEFAULT_18);
		put(StyleFont.MENU_ITEM_LABEL, Font.DEFAULT_16);
	    
		put(StyleIcon.FRAME_CLOSE, Icon.CLOSE);
	    
	}
	//=============================================================================================

	//=============================================================================================
	public Style(Style parent) {
		parent(parent);
	}
	//=============================================================================================
	
	//=============================================================================================
	public Style parent() {
		return parent;
	}
	//=============================================================================================

	//=============================================================================================
	public void parent(Style parent) {
		this.parent = parent;
	}
	//=============================================================================================

	//=============================================================================================
	public Style derive() {
		return new Style(this);
	}
	//=============================================================================================
	
	//=============================================================================================
	public Font get(StyleFont styleFont) {
		var font = fonts.get(styleFont);
		if ((parent != null) && (font == null)) font = parent.get(styleFont);
		return font;
	}
	//=============================================================================================

	//=============================================================================================
	public void put(StyleFont styleFont, Font font) {
		fonts.put(styleFont, font);
	}
	//=============================================================================================
	
	//=============================================================================================
	public Icon get(StyleIcon styleIcon) {
		var icon = icons.get(styleIcon);
		if ((parent != null) && (icon == null)) icon = parent.get(styleIcon);
		return icon;
	}
	//=============================================================================================

	//=============================================================================================
	public void put(StyleIcon styleIcon, Icon icon) {
		icons.put(styleIcon, icon);
	}
	//=============================================================================================
	
	//=============================================================================================
	public Color4f get(StyleColor styleColor) {
		var color = colors.get(styleColor);
		if ((parent != null) && (color == null)) color = parent.get(styleColor);
		return color;
	}
	//=============================================================================================

	//=============================================================================================
	public void put(StyleColor styleColor, Color4f color) {
		colors.put(styleColor, color);
	}
	//=============================================================================================

	//=============================================================================================
	public void put(StyleColor styleColor, float r, float g, float b, float a) {
		colors.put(styleColor, new Color4f(r, g, b, a));
	}
	//=============================================================================================
	
}
//*************************************************************************************************

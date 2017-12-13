package com.mjrousos.breakfastquest.puzzleservice.models;

public enum Items {
	None,
	Breakfast,
	Acorn,
	Apple,
	Carrot,
	Fish,
	Mushroom,
	Pear,
	Strawberry;
	
	public static Items getItemType(short boardValue) {
		byte itemType = (byte)((boardValue >> 8) & 0xff);
		return values()[itemType];		
	}
	
	public static short placeItem(short boardValue, Items itemType) {
		if (getItemType(boardValue) == None) {
			// Place item
			return setItemType(boardValue, itemType);
		}
		else {
			// If an item is already on the tile, leave it unchanged
			return boardValue;
		}
	}
	
	public static short removeItem(short boardValue) {
		return setItemType(boardValue, None);
	}
	
	public static short setItemType(short boardValue, Items itemType) {
		return (short) ((boardValue & 0x00ff) | (itemType.ordinal() << 8));
	}
}

package com.mjrousos.breakfastquest.puzzleservice.models;

public enum Tiles {
	None,
	Grass,
	Log,
	Rock,
	Tree,
	Water,
	BridgedWater,
	LogOnBridgedWater;
	
	public static Tiles getTileType(short boardValue) {
		byte tileType = (byte)boardValue;
		return values()[tileType];		
	}
	
	public static short setTileType(short boardValue, Tiles tileType) {
		return (short) ((boardValue & 0xff00) | tileType.ordinal()); 
	}
	
	public static short addLog(short boardValue) {
		Tiles tileType = getTileType(boardValue);
		switch (tileType) {
			case Grass:
				return setTileType(boardValue, Log);
			case BridgedWater:
				return setTileType(boardValue, LogOnBridgedWater);
			case Water:
				return setTileType(boardValue, BridgedWater);
			default:
				return boardValue;
		}
	}
	
	public static short removeLog(short boardValue) {
		Tiles tileType = getTileType(boardValue);
		switch (tileType) {
			case Log:
				return setTileType(boardValue, Grass);
			case LogOnBridgedWater:
				return setTileType(boardValue, BridgedWater);
			default:
				return boardValue;
		}
	}
}

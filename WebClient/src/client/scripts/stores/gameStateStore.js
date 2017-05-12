import Dispatcher from '../dispatcher/Dispatcher';
import ActionTypes from '../constants/actionTypes';
import InstructionTypes from '../constants/instructionTypes';
import { EventEmitter } from 'events';

import SquareTypes from '../constants/boardSquareTypes';
import { List } from 'immutable';

var getEmptyBoard = function (rows, columns) {
  var board = List();
  for (var i = 0; i < rows; i++) {
    var row = List();
    for (var j = 0; j < columns; j++) {
      row.push(SquareTypes.GRASS);
    }
    board.push(row);
  }

  return board;
};

var getInitialState = function () {
  return {
    animalPosition: { x: 0, y: 0 },
    board: getEmptyBoard(12, 12)
  };
};

var CHANGE_EVENT = 'change';
var gameState = getInitialState();

class GameStateStore extends EventEmitter {

  // React components use this to let us know they'd like to be notified when this store changes
  addChangeListener(callback) {
    this.on(CHANGE_EVENT, callback);
  }

  // React components use this to stop change notifications
  removeChangeListener(callback) {
    this.removeListener(CHANGE_EVENT, callback);
  }

  // Broadcast an event
  emitChange() {
    this.emit(CHANGE_EVENT);
  }

  getAnimalPosition() {
    return gameState.animalPosition;
  }

  getBoard() {
    return gameState.board;
  }

  getGameState() {
    return gameState;
  }
}

const gameStateStore = new GameStateStore();

// Register with the dispatcher
Dispatcher.register(function (action) {
  switch (action.actionType) {
    case ActionTypes.MOVE_ANIMAL:
      var position = gameState.animalPosition;
      switch (action.moveType) {
        case InstructionTypes.MOVE_LEFT:
          position.x = position.x - 1;
          break;
        case InstructionTypes.MOVE_UP:
          position.y = position.y - 1;
          break;
        case InstructionTypes.MOVE_DOWN:
          position.y = position.y + 1;
          break;
        case InstructionTypes.MOVE_RIGHT:
          position.x = position.x + 1;
          break;
        default:
          break;
      }
      gameState.animalPosition = position;
      gameStateStore.emitChange();
      return gameState;
    default:
      break;
  }
});

module.exports = gameStateStore;

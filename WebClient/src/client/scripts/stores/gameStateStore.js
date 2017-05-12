import Dispatcher from '../dispatcher/Dispatcher';
import ActionTypes from '../constants/actionTypes';
import InstructionTypes from '../constants/instructionTypes';
import AnimalTypes from '../constants/animalTypes';
import { EventEmitter } from 'events';

import SquareTypes from '../constants/boardSquareTypes';
import { List } from 'immutable';

var getEmptyBoard = function (rows, columns) {
  var board = List();
  for (var i = 0; i < rows; i++) {
    var row = List();
    for (var j = 0; j < columns; j++) {
      row = row.push(SquareTypes.GRASS);
    }
    board = board.push(row);
  }

  return board;
};

var getInitialState = function () {
  return {
    animal: {
      type: AnimalTypes.OCELOT,
      position: { x: 0, y: 0 }
    },
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
      var position = gameState.animal.position;
      switch (action.moveType) {
        case InstructionTypes.MOVE_LEFT:
          position.x = Math.max(0, position.x - 1);
          break;
        case InstructionTypes.MOVE_UP:
          position.y = Math.max(0, position.y - 1);
          break;
        case InstructionTypes.MOVE_DOWN:
          position.y = Math.min(gameState.board.count() - 1, position.y + 1);
          break;
        case InstructionTypes.MOVE_RIGHT:
          position.x = Math.min(gameState.board.first().count() - 1, position.x + 1);
          break;
        default:
          break;
      }
      gameState.animal.position = position;
      gameStateStore.emitChange();
      return gameState;
    default:
      break;
  }
});

module.exports = gameStateStore;

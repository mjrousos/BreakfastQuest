'use strict';

var Dispatcher = require('../dispatcher/Dispatcher');
var ActionTypes = require('../constants/actionTypes');

var MoveActions = {
  moveAnimal: function (direction) {
    // Dispatch the instruction to all the stores
    Dispatcher.dispatch({
      actionType: ActionTypes.MOVE_ANIMAL,
      moveType: direction
    });
  }
};

module.exports = MoveActions;

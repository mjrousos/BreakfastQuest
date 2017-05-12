'use strict';

import React from 'react';
import InstructionTypes from '../../constants/instructionTypes';
import MoveActions from '../../actions/moveActions';

class InstructionPanel extends React.Component {
  constructor(props) {
    super(props);
  }

  moveAnimal(instruction, event) {
    event.preventDefault();
    console.log('Moving ' + instruction);
    MoveActions.moveAnimal(instruction);
  }

  render() {
    var buttonStyle = {
      height: '35px',
      width: '35px',
      margin: '3px'
    };

    return (
      // Temporary controls
      <div>
        <button style={buttonStyle} type='button' className='btn btn-default' onClick={(event) => { this.moveAnimal(InstructionTypes.MOVE_LEFT, event); }}><span className='glyphicon glyphicon-arrow-left'></span></button>
        <button style={buttonStyle} type='button' className='btn btn-default' onClick={(event) => { this.moveAnimal(InstructionTypes.MOVE_UP, event); }}><span className='glyphicon glyphicon-arrow-up'></span></button>
        <button style={buttonStyle} type='button' className='btn btn-default' onClick={(event) => { this.moveAnimal(InstructionTypes.MOVE_DOWN, event); }}><span className='glyphicon glyphicon-arrow-down'></span></button>
        <button style={buttonStyle} type='button' className='btn btn-default' onClick={(event) => { this.moveAnimal(InstructionTypes.MOVE_RIGHT, event); }}><span className='glyphicon glyphicon-arrow-right'></span></button>
      </div>
    );
  }
}

module.exports = InstructionPanel;

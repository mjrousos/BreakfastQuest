'use strict';

import React from 'react';
import { Component } from 'react';
import GameStateStore from '../../stores/gameStateStore';
import GameBoard from './gameBoard';
import InstructionPanel from './instructionPanel';

class GameBoardPage extends Component {
  constructor(props) {
    super(props);

    this.state = { gameState: GameStateStore.getGameState(), tileSize: 64 };
    this._onChange = this._onChange.bind(this);
  }

  componentWillMount() {
    GameStateStore.addChangeListener(this._onChange);
  }

  componentWillUnmount() {
    GameStateStore.removeChangeListener(this._onChange);
  }

  _onChange() {
    this.setState({ gameState: GameStateStore.getGameState() });
  }

  getStyle() {
    return {
      height: this.state.tileSize * (this.state.gameState.board.count())
    };
  }

  render() {
    return (
      <div className='row' style={this.getStyle()}>
        <div className='col-xs-3'>
          <InstructionPanel />
        </div>
        <div className='col-xs-9'>
          <GameBoard gameState={this.state.gameState} tileSize={this.state.tileSize} />
        </div>
      </div>
    );
  }
}

module.exports = GameBoardPage;

'use strict';

import React from 'react';
import SquareTypes from '../../constants/boardSquareTypes';
import AnimalTypes from '../../constants/animalTypes';
import PropTypes from 'prop-types';

class GameBoard extends React.Component {
  constructor(props) {
    super(props);
  }

  // Image styles
  getAnimalStyle() {
    return {
      position: 'absolute',
      width: this.props.tileSize + 'px',
      left: (this.props.gameState.animal.position.x * this.props.tileSize) + 'px',
      top: (this.props.gameState.animal.position.y * this.props.tileSize) + 'px'
    };
  }

  getSquareStyle(rowNumber, colNumber) {
    return {
      position: 'absolute',
      width: this.props.tileSize + 'px',
      left: (colNumber * this.props.tileSize) + 'px',
      top: (rowNumber * this.props.tileSize) + 'px',
      margin: '0px'
    };
  }

  // Image sources
  getAnimalSrc() {
    switch (this.props.gameState.animal.type) {
      case AnimalTypes.OCELOT:
        return '/img/animals/Ocelot.svg';
      default:
        return '/img/None.svg';
    }
  }

  getSquareSrc(squareType) {
    switch (squareType) {
      case SquareTypes.GRASS:
        return '/img/terrain/Grass-1.svg';
      default:
        return '/img/None.svg';
    }
  }

  // Render methods
  renderGameBoardSquare(squareType, colNumber, row) {
    var rowNumber = row.rowNumber;
    return (
      <img key={'img' + rowNumber + '-' + colNumber} style={this.getSquareStyle(rowNumber, colNumber)} src={this.getSquareSrc(squareType)} />
    );
  }

  renderGameBoardRow(row, rowNumber) {
    row.rowNumber = rowNumber;
    return (
      <div key={'row' + rowNumber}>
        {row.map(this.renderGameBoardSquare, this)}
      </div>
    );
  }

  render() {
    return (
      <div style={{ position: 'relative' }}>
        {this.props.gameState.board.map(this.renderGameBoardRow, this)}
        <img style={this.getAnimalStyle()} src={this.getAnimalSrc()} />
      </div>
    );
  }
}

// Prop types
GameBoard.propTypes = {
  gameState: PropTypes.object.isRequired,
  tileSize: PropTypes.number.isRequired
};

module.exports = GameBoard;

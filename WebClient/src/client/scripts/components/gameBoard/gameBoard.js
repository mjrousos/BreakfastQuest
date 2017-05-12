'use strict';

import React from 'react';
import PropTypes from 'prop-types';

class GameBoard extends React.Component {
  constructor(props) {
    super(props);
  }

  getAnimalStyle() {
    return {
      position: 'relative',
      width: this.props.tileSize + 'px',
      left: (this.props.gameState.animalPosition.x *  this.props.tileSize) + 'px',
      top: (this.props.gameState.animalPosition.y * this.props.tileSize) + 'px'
    };
  }

  render() {
    return (
      <div>
        <img style={this.getAnimalStyle()} src="/img/animals/Ocelot.svg" />
      </div>
    );
  }
}

GameBoard.propTypes = {
  gameState: PropTypes.object.isRequired,
  tileSize: PropTypes.number.isRequired
};

module.exports = GameBoard;

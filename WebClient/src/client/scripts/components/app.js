import React from 'react';
import Header from './common/header';
import GameBoard from './gameBoard/gameBoardPage';

class App extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className="container">
        <Header />
        <GameBoard />
      </div>
    );
  }
}

module.exports = App;

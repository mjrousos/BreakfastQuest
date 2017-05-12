import React from 'react';
import Header from './common/header';
import GameBoard from './gameBoard/gameBoardPage';
import Footer from './common/footer';

class App extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className="container">
        <Header />
        <GameBoard />
        <Footer />
      </div>
    );
  }
}

module.exports = App;

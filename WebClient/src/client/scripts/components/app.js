import React from 'react';

class App extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className="container">
        <div className="jumbotron">
          <h1>Breakfast Quest</h1>
          <p>Can you help a hungry animal find its way to breakfast?</p>
        </div>
        <div>
          <img src="/img/animals/Ocelot.svg" width="64px" />
        </div>
      </div>
    );
  }
}

module.exports = App;

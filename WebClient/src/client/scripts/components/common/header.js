'use strict';

import React from 'react';

class Header extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      /*<nav className="navbar navbar-default">
        <div className="container-fluid">
          <Link to="/" className="navbar-brand">
            <img src="images/logo.png" />
          </Link>
          <ul className="nav navbar-nav">
            <li><Link to="/">Home</Link></li>
            <li><Link to="about">About</Link></li>
          </ul>
        </div>
      </nav>*/
      <div className="jumbotron" style={{paddingBottom: '28px', paddingTop: '16px'}}>
        <h1>Breakfast Quest</h1>
        <p>Can you help a hungry animal find its way to breakfast?</p>
      </div>
    );
  }
}

module.exports = Header;

'use strict';

import React from 'react';

class Footer extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className="row">
        <footer >
          <p className="text-muted">
            Copyright &copy; 2017 M.J. Rousos
          </p>
        </footer>
      </div>
    );
  }
}

module.exports = Footer;

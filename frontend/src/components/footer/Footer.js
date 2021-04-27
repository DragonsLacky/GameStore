import React, { Component } from 'react';

class Footer extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }
  render() {
    return (
      <div className=" bg-dark text-white position-sticky bottom-0">
        <span>@ All rights reserved</span>
      </div>
    );
  }
}

export default Footer;

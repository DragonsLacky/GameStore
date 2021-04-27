import React, { Component } from 'react';
import { Link } from 'react-router-dom';

class DeveloperSingle extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }
  render() {
    return (
      <div className={'p-4'}>
        <Link
          onClick={() => {
            this.props.handleDevSelect(this.props.dev);
          }}
          className={'text-white'}
          to={`/developer/${this.props.dev.id}`}
        >
          <h3>{this.props.dev.name}</h3>
        </Link>
      </div>
    );
  }
}

export default DeveloperSingle;

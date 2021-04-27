import React, { Component } from 'react';
import { Link } from 'react-router-dom';

class PublisherSingle extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }
  render() {
    return (
      <div className={'d-inline-block col-md-5 border rounded m-1'}>
        <div
          className={
            'd-flex flex-row justify-content-around align-items-center'
          }
        >
          <Link
            onClick={() =>
              this.props.handlePublisherSelect(this.props.publisher)
            }
            className={'text-white p-4'}
            to={`/publisher/${this.props.publisher.id}`}
          >
            <h4>{this.props.publisher.name}</h4>
          </Link>
          <p className={'p-4 m-0'}>{this.props.publisher.description}</p>
        </div>
      </div>
    );
  }
}

export default PublisherSingle;

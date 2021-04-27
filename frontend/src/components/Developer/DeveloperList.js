import React, { Component } from 'react';
import { DevService } from '../../service';
import DeveloperSingle from './DeveloperSingle';

class DeveloperList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      developers: [],
    };
  }

  componentDidMount() {
    this.loadDevelopers();
  }

  render() {
    return (
      <div>
        {this.props.byPublisher ? (
          <div className={'d-flex flex-column bg-dark text-white col-md-6'}>
            {this.props.publisher.studios.map((dev) => {
              return (
                <DeveloperSingle
                  dev={dev}
                  handleDevSelect={this.props.handleDevSelect}
                />
              );
            })}
          </div>
        ) : (
          <div
            className={
              'd-flex flex-column bg-dark text-white col-md-12 d-inline-block'
            }
          >
            {this.state.developers.map((dev) => {
              return (
                <DeveloperSingle
                  className={'bg-dark text-white d-inline-block w-50'}
                  dev={dev}
                  handleDevSelect={this.props.handleDevSelect}
                />
              );
            })}
          </div>
        )}
      </div>
    );
  }

  loadDevelopers = () => {
    DevService.fetchDevs().then((response) => {
      console.log(response);
      this.setState({
        developers: response.data,
      });
    });
  };
}

export default DeveloperList;

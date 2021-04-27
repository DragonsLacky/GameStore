import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { AuthService } from '../../service';
import DeveloperList from '../Developer/DeveloperList';

class PublisherPage extends Component {
  constructor(props) {
    super(props);
    this.state = {
      roles: [],
    };
  }
  render() {
    return (
      <div className={'bg-dark text-white'}>
        <div className={'row'}>
          <div className={'col-md-10 offset-1 d-flex'}>
            <div
              className={
                'd-flex flex-column col-md-6 justify-content-center align-items-center p-3'
              }
            >
              <h1>{this.props.publisher.name}</h1>
              <p>{this.props.publisher.description}</p>
              {this.state.roles.includes('ROLE_PUBLISHER') && (
                <Link className={'btn btn-primary w-50'} to={'/dev/add'}>
                  <h5>Create Developer</h5>
                </Link>
              )}
            </div>
            <DeveloperList
              publisher={this.props.publisher}
              handleDevSelect={this.props.handleDevSelect}
              byPublisher={true}
            />
          </div>
        </div>
      </div>
    );
  }

  loadRoles = () => {
    let user = AuthService.getCurrentUser();
    if (user) {
      this.setState({
        roles: user.roles,
      });
    }
  };
}

export default PublisherPage;

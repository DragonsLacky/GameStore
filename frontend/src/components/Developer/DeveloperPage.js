import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { AuthService } from '../../service';
import Games from '../Game/listgames';

class DeveloperPage extends Component {
  constructor(props) {
    super(props);
    this.state = {
      roles: [],
    };
  }
  render() {
    return (
      <div className={'bg-dark text-white h-100'}>
        <div className={'row'}>
          <div className={'col-md-1 offset-1 p-2'}>
            <h1 className={'m-0'}>{this.props.dev.name}</h1>
          </div>
          <div className={'col-md-10'}>
            <div
              className={
                'd-flex flex-row justify-content-end align-items-center h-100'
              }
            >
              {this.state.roles.includes('ROLE_PUBLISHER') && (
                <Link
                  className={'btn btn-primary rounded mr-4'}
                  to={'/game/add'}
                >
                  <h4>Add Game</h4>
                </Link>
              )}
            </div>
          </div>
        </div>
        <div className={'row h-100'}>
          <div className={'col-md-10 offset-1 h-100'}>
            <Games
              display={'dev'}
              criteria={this.props.dev.id}
              handleGameSelect={this.props.handleGameSelect}
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

export default DeveloperPage;

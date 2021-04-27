import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { AuthService, GameService, UserService } from '../../service';
import Games from '../Game/listgames';

class User extends Component {
  constructor(props) {
    super(props);
    this.state = {
      games: [],
      user: {},
      roles: [],
    };
  }

  componentDidMount() {
    this.loadUser();
    this.loadGames();
  }
  render() {
    return (
      <div className={'bg-dark text-white h-100'}>
        <div className={'row'}>
          <div className={'col-md-10 offset-1'}>
            <div className={'row'}>
              <div className={'col-md-3 p-3'}>
                <img
                  className={'d-inline-block rounded-circle'}
                  src={
                    'https://p.kindpng.com/picc/s/78-785827_user-profile-avatar-login-account-male-user-icon.png'
                  }
                  alt={'avatar'}
                />
              </div>
              <div
                className={
                  'col-md-5 d-flex flex-column justify-content-around align-items-baseline'
                }
              >
                <h1>{this.state.user.username}</h1>
              </div>
              <div className={'col-md-4 border'}>
                <div
                  className={
                    'row border-bottom d-flex justify-content-center align-items-center'
                  }
                >
                  <div className={'col-md-12'}>
                    <h2 className={'m-2'}>Stats</h2>
                  </div>
                </div>
                <div className={'row h-75'}>
                  <div
                    className={
                      'col-md-12 d-flex flex-column justify-content-around h-100'
                    }
                  >
                    <div className={'d-flex justify-content-around p-2'}>
                      <h4>Owned: </h4>
                      <h4>{this.state.games.length}</h4>
                    </div>
                    {this.state.roles.filter(
                      (role) => role.name === 'ROLE_PUBLISHER'
                    ).length !== 0 && (
                      <div className={'d-flex justify-content-around p-2'}>
                        <Link
                          className={'btn btn-secondary'}
                          to={'/publishers/owned'}
                        >
                          <h4>View owned publishers</h4>
                        </Link>
                      </div>
                    )}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className={'row border-top'}>
          <div className={'col-md-12'}>
            {this.state.games.length === 0 ? (
              <div className={'h-100 p-5 m-5'}>
                <h1>No owned games</h1>
              </div>
            ) : (
              <Games
                display={'owned'}
                handleGameSelect={this.props.handleGameSelect}
              />
            )}
          </div>
        </div>
      </div>
    );
  }
  loadUser = () => {
    let user = AuthService.getCurrentUser();
    if (user) {
      UserService.loadUser(user.username).then((response) => {
        this.setState({
          user: response.data,
          roles: response.data.roles,
        });
      });
    }
  };

  loadGames = () => {
    let user = AuthService.getCurrentUser();
    if (user) {
      GameService.fetchOwnedGames(user.username).then((response) => {
        this.setState({
          games: response.data,
        });
      });
    }
  };
}

export default User;

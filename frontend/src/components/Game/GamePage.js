import React, { Component } from 'react';
import { Link, NavLink, withRouter } from 'react-router-dom';
import { AuthService, GameService } from '../../service';
import Game from './singleGame';

class GamePage extends Component {
  constructor(props) {
    super(props);
    this.state = {
      imageSrc:
        'https://upload.wikimedia.org/wikipedia/en/thumb/a/a5/Grand_Theft_Auto_V.png/220px-Grand_Theft_Auto_V.png',
      roles: [],
      games: [],
    };
  }

  componentDidMount() {
    this.setState({
      imageSrc: '',
    });
    this.loadRoles();
    this.loadGames();
  }

  render() {
    return (
      <div className={'bg-dark text-white'}>
        <div className={'row'}>
          <div className={'col-md-3 p-3'}>
            <h1 className={'text-capitalize'}>{this.props.game.title}</h1>
          </div>
          {(this.state.roles.includes('ROLE_ADMIN') ||
            this.state.games.filter((game) => game.id === this.props.game.id)
              .length !== 0) && (
            <div
              className={
                'col-md-9 d-flex flex-row justify-content-end align-items-center p-3'
              }
            >
              <button
                onClick={() => this.handleDelete(this.props.game.id)}
                className={'mx-3 btn btn-danger'}
              >
                Delete
              </button>
            </div>
          )}
        </div>
        <div className={'row'} style={{ height: 40 + 'rem' }}>
          <div className={'col-md-7 h-100'}>
            <div className={'row h-100'}>
              <div
                className={
                  'col-md-4 d-flex flex-column align-items-center h-100 pl-4 pr-2'
                }
                style={{
                  'overflow-y': 'scroll',
                  width: 250 + 'px',
                }}
              >
                <NavLink
                  onClick={() =>
                    this.selectImage(
                      'https://upload.wikimedia.org/wikipedia/en/thumb/a/a5/Grand_Theft_Auto_V.png/220px-Grand_Theft_Auto_V.png'
                    )
                  }
                  activeClassName={'border rounded'}
                  to={`${this.props.match.url}/${this.state.imageSrc}`}
                >
                  <img
                    className={
                      'd-inline-block border-0 mx-auto rounded w-100 p-1 py-2'
                    }
                    alt="scene from game"
                    src={
                      'https://upload.wikimedia.org/wikipedia/en/thumb/a/a5/Grand_Theft_Auto_V.png/220px-Grand_Theft_Auto_V.png'
                    }
                  />
                </NavLink>
              </div>
              <div
                className={
                  'col-md-8 d-flex justify-content-center align-items-center'
                }
              >
                <img
                  className={'d-inline-block w-auto p-auto m-auto'}
                  src={this.state.imageSrc}
                  alt={'game'}
                />
              </div>
            </div>
          </div>
          <div className={'col-md-5 border rounded-sm py-3  h-100'}>
            <div style={{ height: 15 + '%' }} className={'row'}>
              <div className={'col-md-12 h-100 pb-2  border-bottom'}>
                <img
                  className={'w-100 h-100'}
                  src={
                    'https://upload.wikimedia.org/wikipedia/en/thumb/a/a5/Grand_Theft_Auto_V.png/220px-Grand_Theft_Auto_V.png'
                  }
                  alt={'game logo'}
                ></img>
              </div>
            </div>
            <div className={'row w-auto h-100'}>
              <div className={'col-md-12 p-3 h-100'}>
                <p className={'text-justify'} style={{ height: 40 + '%' }}>
                  {this.props.game.description}
                </p>
                <div className={'d-flex flex-column justify-content-around'}>
                  <div className={'d-flex flex-row justify-content-baseline'}>
                    <h5 className={'px-2'}>Developer: </h5>
                    <Link
                      className={'text-white'}
                      onClick={() => {
                        this.props.handleDevSelect(this.props.game.developer);
                      }}
                      to={`/developer/${this.props.game.developer.id}`}
                    >
                      <h5 className={'px-2'}>
                        {this.props.game.developer.name}
                      </h5>
                    </Link>
                  </div>
                  <div className={'d-flex flex-row justify-content-baseline'}>
                    <h5 className={'px-2'}>Publisher: </h5>
                    <Link
                      className={'text-white'}
                      onClick={() => {
                        this.props.handlePublisherSelect(
                          this.props.game.publisher
                        );
                      }}
                      to={`/publisher/${this.props.game.publisher.id}`}
                    >
                      <h5 className={'px-2'}>
                        {this.props.game.publisher.name}
                      </h5>
                    </Link>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className={'p-3'}>
          <Game
            game={this.props.game}
            handleGameSelect={this.props.handleGameSelect}
            handleCartOrWishlistAdd={this.handleCartOrWishlistAdd}
          />
        </div>
      </div>
    );
  }

  selectImage = (image) => {
    this.setState({
      imageSrc: image,
    });
  };

  loadRoles = () => {
    let user = AuthService.getCurrentUser();
    if (user) {
      this.setState({
        roles: user.roles,
      });
    }
  };

  loadGames = () => {
    let user = AuthService.getCurrentUser();
    if (user) {
      GameService.fetchCreatedGames(user.username).then((response) => {
        this.setState({
          games: response.data,
        });
      });
    }
  };
  handleCartOrWishlistAdd = () => {
    this.loadGames();
  };
  handleDelete = (gameId) => {
    GameService.deleteGame(gameId).then(() => {
      this.props.history.push('/Store/popular');
      window.location.reload();
    });
  };
}

export default withRouter(GamePage);

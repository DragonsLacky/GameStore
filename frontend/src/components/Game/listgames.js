import React, { Component } from 'react';
import { AuthService, GameService } from '../../service';
import authHeader from '../../service/Auth/auth-header';
import Game from './singleGame';

class Games extends Component {
  constructor(props) {
    super(props);
    if (props.display === 'dev') {
      this.loadGames = this.loadDevGames;
    } else if (props.display === 'owned') {
      this.loadGames = this.loadOwnedGames;
    } else if (props.display === 'genre') {
      this.loadGames = this.loadGamesByGenre;
    } else {
      this.loadGames = this.loadAllGames;
    }
    this.state = {
      games: [],
      show: 5,
    };
  }
  render() {
    return (
      <div className={'text-white bg-dark'}>
        <div className={'container-fluid bg-dark p-3'}>{this.listGames()}</div>
        <div
          onClick={() => this.increaseShow()}
          className={
            'text-center text-white font-weight-bold d-flex align-self-center justify-content-center border-top btn btn-dark'
          }
        >
          More
        </div>
      </div>
    );
  }

  listGames = () => {
    return this.state.games.length === 0 ? (
      <h1 className={'p4'}>No games to display</h1>
    ) : (
      this.state.games
        .filter((v, i, arr) => i < this.state.show)
        .map((game) => {
          return (
            <Game
              handleCartOrWishlistAdd={this.handleCartOrWishlistAdd}
              handleGameSelect={this.props.handleGameSelect}
              game={game}
            />
          );
        })
    );
  };

  increaseShow = () => {
    if (this.state.show < this.state.games.length) {
      this.setState({
        show: this.state.show + 5,
      });
    }
  };

  componentDidMount() {
    this.loadGames(this.props.criteria);
  }

  handleCartOrWishlistAdd = () => {
    this.loadGames(this.props.criteria);
  };

  loadAllGames = () => {
    GameService.fetchGames().then((response) => {
      this.setState({
        games: response.data,
      });
    });
  };

  loadOwnedGames = () => {
    let user = AuthService.getCurrentUser();
    if (user) {
      GameService.fetchOwnedGames(user.username, user.email).then(
        (response) => {
          this.setState({
            games: response.data,
          });
        }
      );
    }
  };

  loadGamesByGenre = (genre) => {
    GameService.fetchGamesByGenre(genre).then((response) => {
      this.setState({
        games: response.data,
      });
    });
  };

  loadDevGames = (devId) => {
    GameService.fetchDevGames(devId).then((response) => {
      this.setState({
        games: response.data,
      });
    });
  };
}

export default Games;

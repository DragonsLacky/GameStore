import React, { Component } from 'react';
import { AuthService, WishlistService } from '../../service';
import WishlistGame from './WishlistGame';

class Wishlist extends Component {
  constructor(props) {
    super(props);
    this.state = {
      games: [],
    };
  }

  componentDidMount() {
    this.loadGames();
  }

  render() {
    return (
      <div className={'bg-dark text-white'}>
        <div className={'container-fluid bg-dark p-3 h-50'}>
          <div className={'row'}>
            <div className={'col-md-10 offset-md-1 border rounded'}>
              {this.listGames().length === 0 ? (
                <div
                  className={
                    'd-flex flex-row justify-content-center align-items-center'
                  }
                >
                  <h1>Wishlist is empty</h1>
                </div>
              ) : (
                this.listGames()
              )}
            </div>
          </div>
          <div className={'bg-dark h-25'}>
            <div className={'row'}>
              <div
                className={
                  'd-flex flex-row col-md-10 offset-1 py-3 border rounded'
                }
              >
                <div className={'col-md-6'}>
                  <div
                    className={
                      'd-flex flex-row justify-content-start align-items-center h-100'
                    }
                  >
                    <h3>Total:</h3>
                    <h1 className={'px-5'}>
                      {this.state.games.length === 0
                        ? 0 + ' $'
                        : this.state.games.length === 1
                        ? this.state.games[0].price
                        : this.state.games.reduce(
                            (v1, v2) => v1.price + v2.price
                          ) + ' $'}
                    </h1>
                  </div>
                </div>
                <div className={'col-md-6'}>
                  <div
                    className={
                      'd-flex flex-row justify-content-end align-items-center h-100'
                    }
                  >
                    <button
                      onClick={this.clearGames}
                      className={'btn btn-danger h-100 p-3 px-3 mx-3'}
                    >
                      <h4>Clear</h4>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
  listGames = () => {
    return this.state.games.map((game) => {
      return (
        <WishlistGame
          handleGameSelect={this.props.handleGameSelect}
          handleGameRemove={this.handleGameRemove}
          game={game}
        />
      );
    });
  };

  handleGameRemove = () => {
    this.loadGames();
  };

  loadGames = () => {
    let user = AuthService.getCurrentUser();
    if (user) {
      WishlistService.fetchWishlistGames(user.username).then((response) => {
        this.setState({
          games: response.data,
        });
      });
    }
  };

  clearGames = () => {
    let user = AuthService.getCurrentUser();
    if (user) {
      WishlistService.clearWishlist(user.username).then((response) => {
        this.setState({
          games: response.data,
        });
      });
    }
  };
}

export default Wishlist;

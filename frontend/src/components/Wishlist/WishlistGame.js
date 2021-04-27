import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { WishlistService } from '../../service';
import AuthService from '../../service/Auth/auth-service';
import CartService from '../../service/Cart/CartService';

class WishlistGame extends Component {
  constructor(props) {
    super(props);
    this.state = {
      cartButton: true,
    };
  }
  componentDidMount() {
    this.loadCartGames();
  }

  render() {
    return (
      <div className={'row border mb-1 text-white'}>
        <div className={'col-md-6 d-flex pl-0'}>
          <Link
            onClick={() => this.props.handleGameSelect(this.props.game)}
            className={'text-white'}
            to={`/games/${this.props.game.id}`}
          >
            <img
              src={
                'https://upload.wikimedia.org/wikipedia/en/thumb/a/a5/Grand_Theft_Auto_V.png/220px-Grand_Theft_Auto_V.png'
              }
              alt="game images"
              style={{ height: 10 + 'rem', width: 10 + 'rem' }}
              className={'mr-3'}
            />
          </Link>
          <div
            className={
              'd-flex flex-column justify-content-around align-items-baseline  p-2'
            }
          >
            <Link
              onClick={() => this.props.handleGameSelect(this.props.game)}
              className={'text-white'}
              to={`/games/${this.props.game.id}`}
            >
              <h4 class="text-capitalize">{this.props.game.title}</h4>
            </Link>
            <div className={'d-flex flex-row'}>
              {this.props.game.genres.map((genre) => {
                return (
                  <h5 className={'text-capitalize px-1'}>
                    {genre.toLowerCase()}
                  </h5>
                );
              })}
            </div>
          </div>
        </div>
        <div className={'col-md-6 pr-0 d-flex justify-content-end'}>
          <div className={'d-flex align-self-center p-3 mr-2'}>
            <span class="font-weight-bold">{this.props.game.price + ' $'}</span>
          </div>
          <div
            className={
              'd-flex flex-column justify-content-center align-items-end'
            }
          >
            <button
              onClick={() => this.handleWishlistRemove(this.props.game.id)}
              className={
                'btn btn-danger h-50 w-100 d-flex align-items-center justify-content-center'
              }
            >
              <span>Remove</span>
            </button>
            {this.state.cartButton && (
              <button
                onClick={() => this.handleCartAdd(this.props.game.id)}
                className={
                  'btn btn-primary h-50 w-100 d-flex align-items-center justify-content-center'
                }
              >
                <span>Cart</span>
              </button>
            )}
          </div>
        </div>
      </div>
    );
  }

  handleWishlistRemove = (gameId) => {
    let user = AuthService.getCurrentUser();
    if (user) {
      WishlistService.removeGameFromWishlist(user.username, gameId).then(() =>
        this.props.handleGameRemove()
      );
    }
  };

  handleCartAdd = (gameId) => {
    let user = AuthService.getCurrentUser();
    if (user) {
      CartService.addGameToCart(user.username, gameId).then(() =>
        this.loadCartGames()
      );
    }
  };

  loadCartGames = () => {
    let user = AuthService.getCurrentUser();
    if (user) {
      CartService.fetchCartGames(user.username).then((response) => {
        this.setState({
          cartButton:
            response.data.filter((game) => game.id === this.props.game.id)
              .length === 0,
        });
      });
    }
  };
}

export default WishlistGame;

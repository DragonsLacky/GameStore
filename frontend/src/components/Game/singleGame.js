import { Component } from 'react';
import { Link } from 'react-router-dom';
import {
  CartService,
  AuthService,
  GameService,
  WishlistService,
} from '../../service';

class Game extends Component {
  constructor(props) {
    super(props);
    this.state = {
      cartButton: true,
      owned: true,
      wishlistButton: true,
    };
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
            {!this.state.owned ? (
              <h2 class="font-weight-bold">{'OWNED'}</h2>
            ) : (
              <h3 class="font-weight-bold">{this.props.game.price + ' $'}</h3>
            )}
          </div>
          <div
            className={
              'd-flex flex-column justify-content-center align-items-end'
            }
          >
            {this.state.owned && this.state.cartButton && (
              <button
                onClick={() => this.handleCartAdd(this.props.game.id)}
                className={
                  'btn btn-primary h-50 w-100 d-flex align-items-center justify-content-center'
                }
              >
                <span>Cart</span>
              </button>
            )}
            {this.state.owned && this.state.wishlistButton && (
              <button
                onClick={() => this.handleWishlistAdd(this.props.game.id)}
                className={
                  'btn btn-primary h-50 w-100 d-flex align-items-center justify-content-center'
                }
                href="#"
              >
                Wishlist
              </button>
            )}
          </div>
        </div>
      </div>
    );
  }

  componentDidMount() {
    this.loadCartGames();
    this.loadOwnedGames();
    this.loadWishlistGames();
  }

  handleCartAdd = (gameId) => {
    let user = AuthService.getCurrentUser();
    if (user) {
      CartService.addGameToCart(user.username, gameId).then((response) => {
        this.props.handleCartOrWishlistAdd();
        this.setState({
          cartButton: false,
        });
      });
    }
  };

  handleWishlistAdd = (gameId) => {
    let user = AuthService.getCurrentUser();
    if (user) {
      WishlistService.addGameToWishlist(user.username, gameId).then(
        (response) => {
          this.props.handleCartOrWishlistAdd();
          this.setState({
            wishlistButton: false,
          });
        }
      );
    }
  };

  loadCartGames = () => {
    let user = AuthService.getCurrentUser();
    if (user) {
      CartService.fetchCartGames(user.username).then((response) => {
        this.setState({
          cartGames: response.data,
          cartButton:
            response.data.filter((game) => game.id === this.props.game.id)
              .length === 0,
        });
      });
    }
  };

  loadWishlistGames = () => {
    let user = AuthService.getCurrentUser();
    if (user) {
      WishlistService.fetchWishlistGames(user.username).then((response) => {
        this.setState({
          wishlistGames: response.data,
          wishlistButton:
            response.data.filter((game) => game.id === this.props.game.id)
              .length === 0,
        });
      });
    }
  };

  loadOwnedGames = () => {
    let user = AuthService.getCurrentUser();
    if (user) {
      GameService.fetchOwnedGames(user.username, user.email).then(
        (response) => {
          this.setState({
            owned:
              response.data.filter((game) => game.id === this.props.game.id)
                .length === 0,
          });
        }
      );
    }
  };
}
export default Game;

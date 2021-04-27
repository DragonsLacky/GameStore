import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import AuthService from '../../service/Auth/auth-service';
import CartService from '../../service/Cart/CartService';

class CartGame extends Component {
  constructor(props) {
    super(props);
    this.state = {};
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
              onClick={() => this.handleCartRemove(this.props.game.id)}
              className={
                'btn btn-danger h-50 w-100 d-flex align-items-center justify-content-center'
              }
            >
              <span>Remove</span>
            </button>
          </div>
        </div>
      </div>
    );
  }

  handleCartRemove = (gameId) => {
    let user = AuthService.getCurrentUser();
    if (user) {
      CartService.removeGameFromCart(user.username, gameId).then(() =>
        this.props.handleGameRemove()
      );
    }
  };
}

export default CartGame;

import React, { Component } from 'react';
import { Link, NavLink, withRouter } from 'react-router-dom';
import logo from '../../resources/logo-social-sq.png';
import { AuthService, GameService, GenreService } from '../../service';

class Navigation extends Component {
  constructor(props) {
    super(props);
    this.state = {
      genres: [],
      user: undefined,
      accountButton: false,
    };
  }
  render() {
    return (
      <nav className={'navbar navbar-expand-lg navbar-dark bg-dark mb-1'}>
        <div className={'d-flex flex-column w-100'}>
          <div className={'row'}>
            <div className={'col-md-2'}>
              <Link className={'navbar-brand h-100'} to={'/Store/popular'}>
                <img src={logo} alt={'logo'} className={'w-100'} />
              </Link>
            </div>
            <div
              className={
                'd-flex justify-content-center align-items-end col-md-7'
              }
            >
              <div>
                <button
                  className={'navbar-toggler'}
                  type={'button'}
                  data-toggle={'collapse'}
                  data-target={'#navbarSupportedContent'}
                  aria-controls={'navbarSupportedContent'}
                  aria-expanded={'false'}
                  aria-label={'Toggle navigation'}
                >
                  <span className="navbar-toggler-icon"></span>
                </button>
                <div
                  className={'collapse navbar-collapse'}
                  id={'navbarSupportedContent'}
                >
                  <ul className={'navbar-nav mr-auto'}>
                    <li className={'nav-item'}>
                      <NavLink
                        activeClassName={'active'}
                        className={'nav-link'}
                        to={'/Store/popular'}
                      >
                        Store
                      </NavLink>
                    </li>
                    <li className={'nav-item'}>
                      <NavLink
                        activeClassName={'active'}
                        className={'nav-link'}
                        to={'/library'}
                      >
                        Library
                      </NavLink>
                    </li>
                    <li className={'nav-item'}>
                      <NavLink
                        activeClassName={'active'}
                        className={'nav-link'}
                        to={'/publishers'}
                      >
                        Publishers
                      </NavLink>
                    </li>
                    <li className={'nav-item'}>
                      <NavLink
                        activeClassName={'active'}
                        className={'nav-link'}
                        to={'/developers'}
                      >
                        Developers
                      </NavLink>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div className={'col-md-3'}>
              <ul
                className={
                  'd-flex flex-row navbar-nav justify-content-end mr-auto'
                }
              >
                <li className={'nav-item px-2'}>
                  <NavLink
                    activeClassName={'active'}
                    className={'nav-link'}
                    to={'/cart'}
                  >
                    Cart
                  </NavLink>
                </li>
                {this.state.accountButton ? (
                  <li className={'nav-item px-2'}>
                    <NavLink
                      activeClassName={'active'}
                      className={'nav-link'}
                      to={`/user/${this.state.user.username}`}
                    >
                      Account
                    </NavLink>
                  </li>
                ) : (
                  <li className={'nav-item px-2'}>
                    <NavLink
                      activeClassName={'active'}
                      className={'nav-link'}
                      to={'/login'}
                    >
                      Login
                    </NavLink>
                  </li>
                )}
                {this.state.accountButton && (
                  <li className={'nav-item px-2'}>
                    <a
                      href={'/login'}
                      onClick={this.logout}
                      activeClassName={'active'}
                      className={'nav-link'}
                      to={`/user/${this.state.user.username}`}
                    >
                      Logout
                    </a>
                  </li>
                )}
              </ul>
            </div>
          </div>
          <div className={'row border-top pt-2 mt-0'}>
            <div className={'col-md-12'}>
              <ul
                className={'navbar-nav d-flex flex-row justify-content-between'}
              >
                <li className={'nav-item dropdown'}>
                  <div
                    className={'nav-link dropdown-toggle'}
                    href="#"
                    id={'navbarDropdown'}
                    role={'button'}
                    data-toggle={'dropdown'}
                    aria-haspopup={'true'}
                    aria-expanded={'false'}
                  >
                    Genre
                  </div>
                  <div
                    className={'dropdown-menu'}
                    aria-labelledby={'navbarDropdown'}
                  >
                    {this.state.genres.map((genre) => {
                      return (
                        <Link
                          onClick={() => {
                            this.props.handleGenreSelect(genre);
                          }}
                          className={'text-capitalize dropdown-item'}
                          to={`/games/genre/${genre.toLowerCase()}`}
                        >
                          {genre.toLowerCase()}
                        </Link>
                      );
                    })}
                  </div>
                </li>
                <li className={'nav-item'}>
                  <form className={'form-inline my-2 my-lg-0'}>
                    <input
                      className={'form-control mr-sm-2'}
                      type={'search'}
                      placeholder={'Search'}
                      aria-label={'Search'}
                    />
                    <button
                      className={' btn btn-outline-success my-2 my-sm-0'}
                      type={'submit'}
                    >
                      Search
                    </button>
                  </form>
                </li>
                <li className={'nav-item border rounded'}>
                  <NavLink
                    activeClassName={'active'}
                    className={'nav-link'}
                    to={'/wishlist'}
                  >
                    Wishlist
                  </NavLink>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </nav>
    );
  }

  componentDidMount() {
    this.loadGenre();
    this.loadUser();
  }

  loadUser = () => {
    const user = AuthService.getCurrentUser();
    if (user) {
      this.setState({
        user: user,
        accountButton: true,
      });
    }
  };

  loadGenre = () => {
    GenreService.fetchGenres().then((data) => {
      this.setState({
        genres: data.data,
      });
    });
  };

  logout = () => {
    AuthService.logout();
  };
}

export default withRouter(Navigation);

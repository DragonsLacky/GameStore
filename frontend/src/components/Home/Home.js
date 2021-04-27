import React, { Component } from 'react';
import { Link, NavLink, Route, Switch, withRouter } from 'react-router-dom';
import '../../../node_modules/bootstrap/dist/js/bootstrap.js';
import { GameService } from '../../service';
import Games from '../Game/listgames';

class Home extends Component {
  constructor(props) {
    super(props);
    this.state = {
      specialOffer: [],
    };
  }
  render() {
    return (
      <div className={'bg-dark'}>
        <div
          id={'carouselExampleIndicators'}
          className={'carousel slide'}
          data-ride="carousel"
        >
          <ol className={'carousel-indicators'}>
            <li
              data-target={'#carouselExampleIndicators'}
              data-slide-to={'0'}
              className={'active'}
            ></li>
            <li
              data-target={'#carouselExampleIndicators'}
              data-slide-to="1"
            ></li>
            <li
              data-target={'#carouselExampleIndicators'}
              data-slide-to="2"
            ></li>
          </ol>
          <div className={'carousel-inner'}>
            <div className={'carousel-item active'}>
              <img
                className={'m-auto d-block'}
                style={{ height: 40 + 'em', width: 80 + 'em' }}
                src={
                  'https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota2_social.jpg'
                }
                alt={'First slide'}
              />
            </div>
            <div className={'carousel-item'}>
              <img
                className={'m-auto d-block'}
                style={{ height: 40 + 'em', width: 80 + 'em' }}
                src="https://www.gametutorials.com/wp-content/uploads/2020/08/cs-go-1170x600.jpg"
                alt="Second slide"
              />
            </div>
            <div className={'carousel-item'}>
              <img
                className={'m-auto d-block'}
                style={{ height: 40 + 'em', width: 80 + 'em' }}
                src={
                  'https://n9e5v4d8.ssl.hwcdn.net/images/longlanding/warframe-metacard.png'
                }
                alt={'Third slide'}
              />
            </div>
          </div>
          <a
            className={'d-flex align-self-end w-25 carousel-control-prev'}
            href={'#carouselExampleIndicators'}
            role={'button'}
            data-slide={'prev'}
          >
            <span
              className={'carousel-control-prev-icon'}
              aria-hidden={'true'}
            ></span>
            <span className={'sr-only'}>Previous</span>
          </a>
          <a
            className={'d-flex align-self-baseline w-25 carousel-control-next'}
            href={'#carouselExampleIndicators'}
            role={'button'}
            data-slide={'next'}
          >
            <span
              className={'carousel-control-next-icon'}
              aria-hidden={'true'}
            ></span>
            <span className={'sr-only'}>Next</span>
          </a>
        </div>
        <div className={'m-3 p-4'}>
          <div className={'row'}>
            <div className={'col-md-2'}>
              <h4 className={'h4 text-white'}>Special Offer</h4>
            </div>
          </div>
          <div className={'row'}>
            <div className={'col-md-10 offset-1'}>
              <div className={'d-flex flex-row justify-content-around'}>
                {this.state.specialOffer.map((game) => {
                  return (
                    <div className={'card'} style={{ width: 18 + 'rem' }}>
                      <img
                        className={'card-img-top'}
                        src={
                          'https://upload.wikimedia.org/wikipedia/en/thumb/a/a5/Grand_Theft_Auto_V.png/220px-Grand_Theft_Auto_V.png'
                        }
                        alt={'Gta v'}
                        style={{ height: 15 + 'rem' }}
                      />
                      <div className={'card-body'}>
                        <h5 className={'card-title'}>{game.title}</h5>
                        <div
                          className={'d-flex flex-row justify-content-between'}
                        >
                          <Link
                            onClick={() => this.props.handleGameSelect(game)}
                            to={`/games/${game.id}`}
                            className={'btn btn-primary px-3 p-2'}
                          >
                            visit
                          </Link>
                          <h3>{game.price}</h3>
                        </div>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
        </div>
        <div className={'row'}>
          <div className={'col-md-10 offset-1'}>
            <ul className={'nav nav-tabs'}>
              <li className={'nav-item'}>
                <NavLink
                  className={'nav-link text-white'}
                  activeClassName={'active text-dark'}
                  to={`${this.props.match.url}/popular`}
                >
                  Popular
                </NavLink>
              </li>
              <li className={'nav-item'}>
                <NavLink
                  className={'nav-link text-white'}
                  activeClassName={'active text-dark'}
                  to={`${this.props.match.url}/special`}
                >
                  Special Offer
                </NavLink>
              </li>
              <li className={'nav-item'}>
                <NavLink
                  className={'nav-link text-white'}
                  activeClassName={'active text-dark'}
                  to={`${this.props.match.url}/alphabetical`}
                >
                  Alphabetical
                </NavLink>
              </li>
            </ul>
            <div>
              <Switch>
                <Route path={`${this.props.match.path}/popular`}>
                  <Games handleGameSelect={this.props.handleGameSelect} />
                </Route>
                <Route path={`${this.props.match.path}/special`}>
                  <Games handleGameSelect={this.props.handleGameSelect} />
                </Route>
                <Route path={`${this.props.match.path}/alphabetical`}>
                  <Games handleGameSelect={this.props.handleGameSelect} />
                </Route>
              </Switch>
            </div>
          </div>
        </div>
      </div>
    );
  }

  componentDidMount() {
    this.loadAllGames();
  }

  loadAllGames = () => {
    GameService.fetchGames().then((data) => {
      this.setState({
        specialOffer: data.data.filter((v, i) => i < 4),
      });
    });
  };
}

export default withRouter(Home);

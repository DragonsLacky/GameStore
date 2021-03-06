import { Component } from 'react';
import { Route, Switch } from 'react-router-dom';
import Footer from '../footer/Footer';
import Games from '../Game/listgames';
import Navigation from '../header/Navigation';
import GamePage from '../Game/GamePage';
import './App.css';
import Home from '../Home/Home';
import DeveloperPage from '../Developer/DeveloperPage';
import LoginPage from '../Auth/LoginPage';
import User from '../User/User';
import CartPage from '../Cart/Cart';
import PublisherAdd from '../Publisher/PublisherAdd';
import PublisherList from '../Publisher/PublisherList';
import PublisherPage from '../Publisher/PublisherPage';
import DevAdd from '../Developer/DevAdd';
import { AuthService } from '../../service';
import GameAdd from '../Game/GameAdd';
import Wishlist from '../Wishlist/Wishlist';
import DeveloperList from '../Developer/DeveloperList';
import Register from '../Auth/RegisterPage';
import GameEdit from '../Game/GameEdit';
import PublisherEdit from '../Publisher/PublisherEdit';
import DeveloperEdit from '../Developer/DeveloperEdit';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      game: {},
      developer: {},
      publisher: {},
      user: {},
      genre: '',
      search: ''
    };
  }
  render() {
    return (
      <div className="App container-fluid">
        <Navigation handleGenreSelect={this.handleGenreSelect} handleSetSearch={this.handleSetSearch} />
        <Switch>
          <Route path={'/game/add'}>
            <GameAdd
              dev={this.state.developer}
              publisher={this.state.publisher}
            />
          </Route>
          <Route path={'/game/edit/:id'}>
            <GameEdit
              dev={this.state.developer}
              publisher={this.state.publisher}
              game={this.state.game}
            />
          </Route>
          <Route path="/games/genre/:id">
            <Games
              handleDevSelect={this.handleDevSelect}
              handlePublisherSelect={this.handlePublisherSelect}
              handleGameSelect={this.handleGameSelect}
              display={'genre'}
              criteria={this.state.genre}
            />
          </Route>
          <Route path="/games/search">
            <Games
                handleDevSelect={this.handleDevSelect}
                handlePublisherSelect={this.handlePublisherSelect}
                handleGameSelect={this.handleGameSelect}
                display={'search'}
                criteria={this.state.search}
            />
          </Route>
          <Route path={'/games/:id'}>
            <GamePage
                game={this.state.game}
                handleGameSelect={this.handleGameSelect}
                handleDevSelect={this.handleDevSelect}
                handlePublisherSelect={this.handlePublisherSelect}
            />
          </Route>
          <Route path={'/login'}>
            <LoginPage />
          </Route>
          <Route path={'/register'}>
            <Register />
          </Route>
          <Route path={'/developer/edit/:id'}>
            <DeveloperEdit developer={this.state.developer} />
          </Route>
          <Route path={'/developer/:id'}>
            <DeveloperPage
              handlePublisherSelect={this.handlePublisherSelect}
              handleDevSelect={this.handleDevSelect}
              handleGameSelect={this.handleGameSelect}
              dev={this.state.developer}
            />
          </Route>
          <Route path={'/developers'}>
            <DeveloperList handleDevSelect={this.handleDevSelect} />
          </Route>
          <Route path={'/user'}>
            <User
              handleGameSelect={this.handleGameSelect}
              handleDevSelect={this.handleDevSelect}
            />
          </Route>
          <Route path={'/publisher/edit/:id'}>
            <PublisherEdit publisher={this.state.publisher} />
          </Route>
          <Route path={'/publisher/add'}>
            <PublisherAdd />
          </Route>
          <Route path={'/publisher/:id'}>
            <PublisherPage
              handlePublisherSelect={this.handlePublisherSelect}
              handleDevSelect={this.handleDevSelect}
              publisher={this.state.publisher}
            />
          </Route>
          <Route path={'/publishers/owned'}>
            <PublisherList
              handlePublisherSelect={this.handlePublisherSelect}
              listBy={'owned'}
            />
          </Route>
          <Route path={'/publishers'}>
            <PublisherList handlePublisherSelect={this.handlePublisherSelect} />
          </Route>
          <Route path={'/dev/add'}>
            <DevAdd publisher={this.state.publisher} />
          </Route>
          <Route path="/library">
            <Games handleGameSelect={this.handleGameSelect} display={'owned'} />
          </Route>
          <Route path="/Store">
            <Home handleGameSelect={this.handleGameSelect} />
          </Route>
          <Route path="/Cart">
            <CartPage handleGameSelect={this.handleGameSelect} />
          </Route>
          <Route path="/wishlist">
            <Wishlist />
          </Route>
          <Route path="/">
            <Home handleGameSelect={this.handleGameSelect} />
          </Route>
        </Switch>
        <Footer />
      </div>
    );
  }

  componentDidMount() {
    this.getUser();
  }

  handleGameSelect = (game) => {
    this.setState({
      game: game,
    });
  };

  handleDevSelect = (dev) => {
    this.setState({
      developer: dev,
    });
  };

  handlePublisherSelect = (publisher) => {
    this.setState({
      publisher: publisher,
    });
  };

  handleSetSearch = (search) => {
    this.setState({
      search: search
    })
  }

  handleGenreSelect = (genre) => {
    this.setState({
      genre: genre,
    });
  };

  getUser = () => {
    let user = AuthService.getCurrentUser();
    if (user) {
      this.setState({
        user: user,
      });
    }
  };
}

export default App;

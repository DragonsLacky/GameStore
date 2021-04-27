import React, { Component } from 'react';
import { GameService, AuthService, GenreService } from '../../service';
import Form from 'react-validation/build/form';
import Input from 'react-validation/build/input';
import CheckButton from 'react-validation/build/button';
import TextArea from 'react-validation/build/textarea';

import { withRouter } from 'react-router';

const required = (value) => {
  if (!value) {
    return (
      <div className="alert alert-danger" role="alert">
        This field is required!
      </div>
    );
  }
};

const number = (value) => {
  if (!parseFloat(value)) {
    return (
      <div className="alert alert-danger" role="alert">
        This field needs to be a number!
      </div>
    );
  }
};

class GameEdit extends Component {
  constructor(props) {
    super(props);
    this.handleGameEdit = this.handleGameEdit.bind(this);
    this.onChangeTitle = this.onChangeTitle.bind(this);
    this.onChangeGenre = this.onChangeGenre.bind(this);
    this.onChangePrice = this.onChangePrice.bind(this);
    this.onChangeDescription = this.onChangeDescription.bind(this);
    this.state = {
      title: this.props.game.title,
      selectedGenre: this.props.game.genres,
      genres: [],
      description: this.props.game.description,
      price: this.props.game.price,
      loading: false,
      message: '',
    };
  }

  componentDidMount() {
    this.loadGenres();
  }
  onChangeTitle(e) {
    this.setState({
      title: e.target.value,
    });
  }

  onChangePrice(e) {
    this.setState({
      price: e.target.value,
    });
  }

  onChangeDescription(e) {
    if (parseFloat(e.target.value) && e.target.value !== '') {
      this.setState({
        description: e.target.value,
      });
    }
  }

  onChangeGenre(e) {
    let genres = this.state.selectedGenre;
    if (genres.includes(e.target.value)) {
      this.setState({
        selectedGenre: genres.filter((genre) => genre !== e.target.value),
      });
    } else {
      genres.push(e.target.value);
      this.setState({
        selectedGenre: genres,
      });
    }
  }

  render() {
    return (
      <div className={'bg-dark text-white'}>
        <div className={'row'}>
          <div className={'col-md-12'}>
            <div className={'d-flex justify-content-center'}>
              <div className={'card bg-dark p-5 w-50'}>
                <div className={'mb-4'}>
                  <h3>Adding a new Game</h3>
                </div>
                <Form
                  onSubmit={this.handleGameEdit}
                  ref={(c) => {
                    this.form = c;
                  }}
                >
                  <div className={'d-flex flex-row'}>
                    <div
                      className={
                        'form-group w-100 d-flex py-2 justify-content-around align-items-center'
                      }
                    >
                      <div className={'w-25'}>
                        <label className={'m-0'} htmlFor="title">
                          Title
                        </label>
                      </div>
                      <div className={'w-75'}>
                        <Input
                          type="text"
                          className="form-control"
                          name="title"
                          value={this.state.title}
                          onChange={this.onChangeTitle}
                          validations={[required]}
                        />
                      </div>
                    </div>
                  </div>
                  <div className={'d-flex flex-row py-2'}>
                    <div
                      className={
                        'd-flex justify-content-center align-items-center w-100'
                      }
                    >
                      <label className={'m-0 px-4'} htmlFor={'description'}>
                        Description
                      </label>
                      <TextArea
                        className={'form-control w-100'}
                        name={'description'}
                        value={this.state.description}
                        onChange={this.onChangeDescription}
                      ></TextArea>
                    </div>
                    <div
                      className={
                        'form-group w-50 d-flex justify-content-around align-items-center'
                      }
                    >
                      <label className={'m-0'} htmlFor="price">
                        Price
                      </label>
                      <Input
                        type="number"
                        className="form-control"
                        name="price"
                        value={this.state.price}
                        onChange={this.onChangePrice}
                        validations={[required, number]}
                      />
                    </div>
                  </div>
                  <div className="form-group w-100"></div>
                  <div className="form-group">
                    {this.state.genres.map((genre) => {
                      return (
                        <div className={'d-inline-block w-25'}>
                          <div
                            className={
                              'd-flex flex-row justify-content-around align-items-center'
                            }
                          >
                            <label
                              htmlFor={genre}
                              className={'m-0 text-capitalize'}
                            >
                              {genre.toLowerCase()}
                            </label>
                            <Input
                              type={'checkbox'}
                              className={'form-control'}
                              checked={this.state.selectedGenre.includes(genre)}
                              name={genre}
                              value={genre}
                              onChange={this.onChangeGenre}
                            />
                          </div>
                        </div>
                      );
                    })}
                  </div>
                  <div className="form-group">
                    <button
                      className="btn btn-primary btn-block"
                      disabled={this.state.loading}
                    >
                      {this.state.loading && (
                        <span className="spinner-border spinner-border-sm"></span>
                      )}
                      <span>Edit</span>
                    </button>
                  </div>
                  {this.state.message && (
                    <div className="form-group">
                      <div className="alert alert-danger" role="alert">
                        {this.state.message}
                      </div>
                    </div>
                  )}
                  <CheckButton
                    style={{ display: 'none' }}
                    ref={(c) => {
                      this.checkBtn = c;
                    }}
                  />
                </Form>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  loadGenres = () => {
    GenreService.fetchGenres().then((response) => {
      this.setState({
        genres: response.data,
      });
    });
  };

  handleGameEdit(e) {
    e.preventDefault();

    this.setState({
      message: '',
      loading: true,
    });

    this.form.validateAll();

    if (this.checkBtn.context._errors.length === 0) {
      let user = AuthService.getCurrentUser();
      GameService.editGame(
        this.props.game.id,
        user.username,
        this.state.title,
        this.state.description,
        this.state.price,
        this.props.game.developer.id,
        this.props.game.publisher.id,
        this.state.selectedGenre
      ).then(
        () => {
          this.props.history.push('/Store/popular');
          window.location.reload();
        },
        (error) => {
          const resMessage =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();

          this.setState({
            loading: false,
            message: resMessage,
          });
        }
      );
    } else {
      this.setState({
        loading: false,
      });
    }
  }
}

export default withRouter(GameEdit);

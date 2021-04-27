import React, { Component } from 'react';
import Form from 'react-validation/build/form';
import Input from 'react-validation/build/input';
import CheckButton from 'react-validation/build/button';
import { withRouter } from 'react-router';
import { PublisherService, AuthService } from '../../service';

const required = (value) => {
  if (!value) {
    return (
      <div className="alert alert-danger" role="alert">
        This field is required!
      </div>
    );
  }
};

class PublisherEdit extends Component {
  constructor(props) {
    super(props);
    this.handlePublisherEdit = this.handlePublisherEdit.bind(this);
    this.onChangeName = this.onChangeName.bind(this);
    this.onChangeDescription = this.onChangeDescription.bind(this);
    console.log(props);
    this.state = {
      name: this.props.publisher.name,
      description: this.props.publisher.description,
      loading: false,
      message: '',
    };
  }
  onChangeName(e) {
    this.setState({
      name: e.target.value,
    });
  }

  onChangeDescription(e) {
    this.setState({
      description: e.target.value,
    });
  }

  render() {
    return (
      <div className={'bg-dark text-white'}>
        <div className={'row'}>
          <div className={'col-md-12'}>
            <div className={'d-flex justify-content-center'}>
              <div className={'card bg-dark p-5'}>
                <div>
                  <h3>Editing a publisher</h3>
                </div>
                <Form
                  onSubmit={this.handlePublisherEdit}
                  ref={(c) => {
                    this.form = c;
                  }}
                >
                  <div className="form-group">
                    <label htmlFor="name">Name</label>
                    <Input
                      type="text"
                      className="form-control"
                      name="Name"
                      value={this.state.name}
                      onChange={this.onChangeName}
                      validations={[required]}
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="description">Description</label>
                    <Input
                      type="description"
                      className="form-control"
                      name="description"
                      value={this.state.description}
                      onChange={this.onChangeDescription}
                      validations={[required]}
                    />
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

  handlePublisherEdit(e) {
    e.preventDefault();

    this.setState({
      message: '',
      loading: true,
    });

    this.form.validateAll();

    if (this.checkBtn.context._errors.length === 0) {
      let user = AuthService.getCurrentUser();
      PublisherService.editPublisher(
        this.props.publisher.id,
        this.state.name,
        this.state.description,
        user.username
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

export default withRouter(PublisherEdit);

import React, { Component } from 'react';
import Form from 'react-validation/build/form';
import Input from 'react-validation/build/input';
import CheckButton from 'react-validation/build/button';
import AuthService from '../../service/Auth/auth-service';
import { withRouter } from 'react-router';
import DevService from '../../service/Developer/DevService';

const required = (value) => {
  if (!value) {
    return (
      <div className="alert alert-danger" role="alert">
        This field is required!
      </div>
    );
  }
};

class DeveloperAdd extends Component {
  constructor(props) {
    super(props);
    this.handleDevCreate = this.handleDevCreate.bind(this);
    this.onChangeName = this.onChangeName.bind(this);
    this.state = {
      name: '',
      loading: false,
      message: '',
    };
  }
  onChangeName(e) {
    this.setState({
      name: e.target.value,
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
                  <h3>Creating a new publisher</h3>
                </div>
                <Form
                  onSubmit={this.handleDevCreate}
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
                    <button
                      className="btn btn-primary btn-block"
                      disabled={this.state.loading}
                    >
                      {this.state.loading && (
                        <span className="spinner-border spinner-border-sm"></span>
                      )}
                      <span>Add</span>
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

  handleDevCreate(e) {
    e.preventDefault();

    this.setState({
      message: '',
      loading: true,
    });

    this.form.validateAll();

    if (this.checkBtn.context._errors.length === 0) {
      let user = AuthService.getCurrentUser();
      DevService.createDev(
        this.state.name,
        this.props.publisher.id,
        user.username
      ).then(
        () => {
          this.props.history.push('/Publishers/owned');
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

export default withRouter(DeveloperAdd);

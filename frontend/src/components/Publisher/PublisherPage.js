import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { AuthService, PublisherService } from '../../service';
import DeveloperList from '../Developer/DeveloperList';

class PublisherPage extends Component {
  constructor(props) {
    super(props);
    this.state = {
      roles: [],
    };
  }

  componentDidMount() {
    console.log('hello')
    this.loadRoles();
  }

  render() {
    return (
      <div className={'bg-dark text-white'}>
        <div className={'row'}>
          <div className={'col-md-10 offset-1 d-flex'}>
            <div
              className={
                'd-flex flex-column col-md-6 justify-content-around align-items-center p-3'
              }
            >
              <h1>{this.props.publisher.name}</h1>
              <p>{this.props.publisher.description}</p>
              {this.state.roles.includes('ROLE_PUBLISHER') && (
                <Link className={'btn btn-primary w-50'} to={'/dev/add'}>
                  <h5>Create Developer</h5>
                </Link>
              )}
              {this.state.roles.includes('ROLE_PUBLISHER') && (
                <Link
                  onClick={() =>
                    this.props.handlePublisherSelect(this.props.publisher)
                  }
                  to={`/publisher/edit/${this.props.publisher.id}`}
                  className={'btn btn-primary m-4 w-50'}
                >
                  <h5>Edit Publisher</h5>
                </Link>
              )}
              {this.state.roles.includes('ROLE_PUBLISHER') && (
                <button
                  onClick={() => this.handleDelete(this.props.publisher.id)}
                  className={'btn btn-danger w-50'}
                >
                  <h5>Delete Publisher</h5>
                </button>
              )}
            </div>
            <DeveloperList
              publisher={this.props.publisher}
              handleDevSelect={this.props.handleDevSelect}
              byPublisher={true}
            />
          </div>
        </div>
      </div>
    );
  }

  loadRoles = () => {
    let user = AuthService.getCurrentUser();
    if (user) {
      this.setState({
        roles: user.roles,
      });
    }
  };

  handleDelete = (publisherId) => {
    PublisherService.deletePublisher(publisherId).then((response) => {
      this.props.history.push('/Store/popular');
      window.location.reload();
    });
  };
}

export default withRouter(PublisherPage);

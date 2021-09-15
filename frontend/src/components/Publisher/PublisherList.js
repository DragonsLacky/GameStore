import React, { Component } from 'react';
import { AuthService } from '../../service';
import PublisherService from '../../service/Publisher/PublisherService';
import PublisherSingle from './PublisherSingle';
import {Link} from "react-router-dom";

class PublisherList extends Component {
  constructor(props) {
    super(props);

    if (props.listBy === 'owned') {
      this.loadPublishers = this.loadOwnedPublishers;
    } else {
      this.loadPublishers = this.loadAllPublishers;
    }
    this.state = {
      publishers: [],
      roles: []
    };
  }

  componentDidMount() {
    this.loadPublishers();
    this.loadRoles();
  }

  render() {
    return (
      <div className={'bg-dark text-white h-100'}>
        <div className={'row py-3'}>
          <div className={'col-md-10 offset-1 mb-2'} >
            {this.state.roles.includes('ROLE_PUBLISHER') && (
                <Link className={'btn btn-primary w-50'} to={'/publisher/add'}>
                  <h5>Create Publisher</h5>
                </Link>
            )}
          </div>
          <div className={'col-md-10 offset-1'}>
            {this.state.publishers.map((publisher) => {
              return (
                <PublisherSingle
                  handlePublisherSelect={this.props.handlePublisherSelect}
                  publisher={publisher}
                />
              );
            })}
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

  loadAllPublishers = () => {
    PublisherService.fetchPublishers().then((response) => {
      this.setState({
        publishers: response.data,
      });
    });
  };

  loadOwnedPublishers = () => {
    let user = AuthService.getCurrentUser();
    if (user) {
      PublisherService.fetchOwnedPublishers(user.username).then((response) => {
        this.setState({
          publishers: response.data,
        });
      });
    }
  };
}

export default PublisherList;

import React, { Component } from 'react';
import { AuthService } from '../../service';
import PublisherService from '../../service/Publisher/PublisherService';
import PublisherSingle from './PublisherSingle';

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
    };
  }

  componentDidMount() {
    this.loadPublishers();
  }

  render() {
    return (
      <div className={'bg-dark text-white h-100'}>
        <div className={'row py-3'}>
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

import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { IApplication } from '@/shared/model/application.model';
import ApplicationService from './application.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ApplicationDetails extends mixins(JhiDataUtils) {
  @Inject('applicationService') private applicationService: () => ApplicationService;
  @Inject('alertService') private alertService: () => AlertService;

  public application: IApplication = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.applicationId) {
        vm.retrieveApplication(to.params.applicationId);
      }
    });
  }

  public retrieveApplication(applicationId) {
    this.applicationService()
      .find(applicationId)
      .then(res => {
        this.application = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

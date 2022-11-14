import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITech } from '@/shared/model/tech.model';
import TechService from './tech.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class TechDetails extends Vue {
  @Inject('techService') private techService: () => TechService;
  @Inject('alertService') private alertService: () => AlertService;

  public tech: ITech = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.techId) {
        vm.retrieveTech(to.params.techId);
      }
    });
  }

  public retrieveTech(techId) {
    this.techService()
      .find(techId)
      .then(res => {
        this.tech = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

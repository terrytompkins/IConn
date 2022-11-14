import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { maxLength, required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';

import TechService from '@/entities/tech/tech.service';
import { ITech } from '@/shared/model/tech.model';

import { IApplication, Application } from '@/shared/model/application.model';
import ApplicationService from './application.service';

const validations: any = {
  application: {
    name: {},
    description: {
      maxLength: maxLength(4000),
    },
    wikiUrl: {},
    logoImage: {},
    createdAt: {
      required,
    },
    modifiedAt: {
      required,
    },
    user: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class ApplicationUpdate extends mixins(JhiDataUtils) {
  @Inject('applicationService') private applicationService: () => ApplicationService;
  @Inject('alertService') private alertService: () => AlertService;

  public application: IApplication = new Application();

  @Inject('userService') private userService: () => UserService;

  public users: Array<any> = [];

  @Inject('techService') private techService: () => TechService;

  public teches: ITech[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.applicationId) {
        vm.retrieveApplication(to.params.applicationId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
    this.application.teches = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.application.id) {
      this.applicationService()
        .update(this.application)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('iConnApp.application.updated', { param: param.id });
          return (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.applicationService()
        .create(this.application)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('iConnApp.application.created', { param: param.id });
          (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.application[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.application[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.application[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.application[field] = null;
    }
  }

  public retrieveApplication(applicationId): void {
    this.applicationService()
      .find(applicationId)
      .then(res => {
        res.createdAt = new Date(res.createdAt);
        res.modifiedAt = new Date(res.modifiedAt);
        this.application = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public clearInputImage(field, fieldContentType, idInput): void {
    if (this.application && field && fieldContentType) {
      if (Object.prototype.hasOwnProperty.call(this.application, field)) {
        this.application[field] = null;
      }
      if (Object.prototype.hasOwnProperty.call(this.application, fieldContentType)) {
        this.application[fieldContentType] = null;
      }
      if (idInput) {
        (<any>this).$refs[idInput] = null;
      }
    }
  }

  public initRelationships(): void {
    this.userService()
      .retrieve()
      .then(res => {
        this.users = res.data;
      });
    this.techService()
      .retrieve()
      .then(res => {
        this.teches = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      return selectedVals.find(value => option.id === value.id) ?? option;
    }
    return option;
  }
}

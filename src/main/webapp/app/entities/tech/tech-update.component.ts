import { Component, Vue, Inject } from 'vue-property-decorator';

import { maxLength, required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';

import ApplicationService from '@/entities/application/application.service';
import { IApplication } from '@/shared/model/application.model';

import { ITech, Tech } from '@/shared/model/tech.model';
import TechService from './tech.service';
import { TechType } from '@/shared/model/enumerations/tech-type.model';

const validations: any = {
  tech: {
    name: {},
    techType: {},
    description: {
      maxLength: maxLength(2000),
    },
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
export default class TechUpdate extends Vue {
  @Inject('techService') private techService: () => TechService;
  @Inject('alertService') private alertService: () => AlertService;

  public tech: ITech = new Tech();

  @Inject('userService') private userService: () => UserService;

  public users: Array<any> = [];

  @Inject('applicationService') private applicationService: () => ApplicationService;

  public applications: IApplication[] = [];
  public techTypeValues: string[] = Object.keys(TechType);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.techId) {
        vm.retrieveTech(to.params.techId);
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
  }

  public save(): void {
    this.isSaving = true;
    if (this.tech.id) {
      this.techService()
        .update(this.tech)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('iConnApp.tech.updated', { param: param.id });
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
      this.techService()
        .create(this.tech)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('iConnApp.tech.created', { param: param.id });
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
      this.tech[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.tech[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.tech[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.tech[field] = null;
    }
  }

  public retrieveTech(techId): void {
    this.techService()
      .find(techId)
      .then(res => {
        res.createdAt = new Date(res.createdAt);
        res.modifiedAt = new Date(res.modifiedAt);
        this.tech = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.userService()
      .retrieve()
      .then(res => {
        this.users = res.data;
      });
    this.applicationService()
      .retrieve()
      .then(res => {
        this.applications = res.data;
      });
  }
}

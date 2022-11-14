import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import ApplicationService from './application/application.service';
import TechService from './tech/tech.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('applicationService') private applicationService = () => new ApplicationService();
  @Provide('techService') private techService = () => new TechService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}

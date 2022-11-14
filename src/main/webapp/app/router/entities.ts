import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

// prettier-ignore
const Application = () => import('@/entities/application/application.vue');
// prettier-ignore
const ApplicationUpdate = () => import('@/entities/application/application-update.vue');
// prettier-ignore
const ApplicationDetails = () => import('@/entities/application/application-details.vue');
// prettier-ignore
const Tech = () => import('@/entities/tech/tech.vue');
// prettier-ignore
const TechUpdate = () => import('@/entities/tech/tech-update.vue');
// prettier-ignore
const TechDetails = () => import('@/entities/tech/tech-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'application',
      name: 'Application',
      component: Application,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application/new',
      name: 'ApplicationCreate',
      component: ApplicationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application/:applicationId/edit',
      name: 'ApplicationEdit',
      component: ApplicationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application/:applicationId/view',
      name: 'ApplicationView',
      component: ApplicationDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tech',
      name: 'Tech',
      component: Tech,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tech/new',
      name: 'TechCreate',
      component: TechUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tech/:techId/edit',
      name: 'TechEdit',
      component: TechUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tech/:techId/view',
      name: 'TechView',
      component: TechDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};

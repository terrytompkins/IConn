/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import TechUpdateComponent from '@/entities/tech/tech-update.vue';
import TechClass from '@/entities/tech/tech-update.component';
import TechService from '@/entities/tech/tech.service';

import UserService from '@/entities/user/user.service';

import ApplicationService from '@/entities/application/application.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Tech Management Update Component', () => {
    let wrapper: Wrapper<TechClass>;
    let comp: TechClass;
    let techServiceStub: SinonStubbedInstance<TechService>;

    beforeEach(() => {
      techServiceStub = sinon.createStubInstance<TechService>(TechService);

      wrapper = shallowMount<TechClass>(TechUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          techService: () => techServiceStub,
          alertService: () => new AlertService(),

          userService: () => new UserService(),

          applicationService: () =>
            sinon.createStubInstance<ApplicationService>(ApplicationService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('load', () => {
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.tech = entity;
        techServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(techServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.tech = entity;
        techServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(techServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTech = { id: 123 };
        techServiceStub.find.resolves(foundTech);
        techServiceStub.retrieve.resolves([foundTech]);

        // WHEN
        comp.beforeRouteEnter({ params: { techId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.tech).toBe(foundTech);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});

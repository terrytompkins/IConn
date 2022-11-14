/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TechDetailComponent from '@/entities/tech/tech-details.vue';
import TechClass from '@/entities/tech/tech-details.component';
import TechService from '@/entities/tech/tech.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Tech Management Detail Component', () => {
    let wrapper: Wrapper<TechClass>;
    let comp: TechClass;
    let techServiceStub: SinonStubbedInstance<TechService>;

    beforeEach(() => {
      techServiceStub = sinon.createStubInstance<TechService>(TechService);

      wrapper = shallowMount<TechClass>(TechDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { techService: () => techServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTech = { id: 123 };
        techServiceStub.find.resolves(foundTech);

        // WHEN
        comp.retrieveTech(123);
        await comp.$nextTick();

        // THEN
        expect(comp.tech).toBe(foundTech);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTech = { id: 123 };
        techServiceStub.find.resolves(foundTech);

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

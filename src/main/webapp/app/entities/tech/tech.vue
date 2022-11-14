<template>
  <div>
    <h2 id="page-heading" data-cy="TechHeading">
      <span v-text="$t('iConnApp.tech.home.title')" id="tech-heading">Teches</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('iConnApp.tech.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'TechCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-tech">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('iConnApp.tech.home.createLabel')"> Create a new Tech </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && teches && teches.length === 0">
      <span v-text="$t('iConnApp.tech.home.notFound')">No teches found</span>
    </div>
    <div class="table-responsive" v-if="teches && teches.length > 0">
      <table class="table table-striped" aria-describedby="teches">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span v-text="$t('iConnApp.tech.name')">Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('techType')">
              <span v-text="$t('iConnApp.tech.techType')">Tech Type</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'techType'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('description')">
              <span v-text="$t('iConnApp.tech.description')">Description</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('createdAt')">
              <span v-text="$t('iConnApp.tech.createdAt')">Created At</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createdAt'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('modifiedAt')">
              <span v-text="$t('iConnApp.tech.modifiedAt')">Modified At</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'modifiedAt'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('user.login')">
              <span v-text="$t('iConnApp.tech.user')">User</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'user.login'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="tech in teches" :key="tech.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TechView', params: { techId: tech.id } }">{{ tech.id }}</router-link>
            </td>
            <td>{{ tech.name }}</td>
            <td v-text="$t('iConnApp.TechType.' + tech.techType)">{{ tech.techType }}</td>
            <td>{{ tech.description }}</td>
            <td>{{ tech.createdAt ? $d(Date.parse(tech.createdAt), 'short') : '' }}</td>
            <td>{{ tech.modifiedAt ? $d(Date.parse(tech.modifiedAt), 'short') : '' }}</td>
            <td>
              {{ tech.user ? tech.user.login : '' }}
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'TechView', params: { techId: tech.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'TechEdit', params: { techId: tech.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(tech)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="iConnApp.tech.delete.question" data-cy="techDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-tech-heading" v-text="$t('iConnApp.tech.delete.question', { id: removeId })">
          Are you sure you want to delete this Tech?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-tech"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeTech()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="teches && teches.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./tech.component.ts"></script>

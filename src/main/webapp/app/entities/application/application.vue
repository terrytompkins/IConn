<template>
  <div>
    <h2 id="page-heading" data-cy="ApplicationHeading">
      <span v-text="$t('iConnApp.application.home.title')" id="application-heading">Applications</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('iConnApp.application.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ApplicationCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-application"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('iConnApp.application.home.createLabel')"> Create a new Application </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && applications && applications.length === 0">
      <span v-text="$t('iConnApp.application.home.notFound')">No applications found</span>
    </div>
    <div class="table-responsive" v-if="applications && applications.length > 0">
      <table class="table table-striped" aria-describedby="applications">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span v-text="$t('iConnApp.application.name')">Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('description')">
              <span v-text="$t('iConnApp.application.description')">Description</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('wikiUrl')">
              <span v-text="$t('iConnApp.application.wikiUrl')">Wiki Url</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'wikiUrl'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('logoImage')">
              <span v-text="$t('iConnApp.application.logoImage')">Logo Image</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'logoImage'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('createdAt')">
              <span v-text="$t('iConnApp.application.createdAt')">Created At</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createdAt'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('modifiedAt')">
              <span v-text="$t('iConnApp.application.modifiedAt')">Modified At</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'modifiedAt'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('user.login')">
              <span v-text="$t('iConnApp.application.user')">User</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'user.login'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="application in applications" :key="application.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: application.id } }">{{ application.id }}</router-link>
            </td>
            <td>{{ application.name }}</td>
            <td>{{ application.description }}</td>
            <td>{{ application.wikiUrl }}</td>
            <td>
              <a v-if="application.logoImage" v-on:click="openFile(application.logoImageContentType, application.logoImage)">
                <img
                  v-bind:src="'data:' + application.logoImageContentType + ';base64,' + application.logoImage"
                  style="max-height: 30px"
                  alt="application image"
                />
              </a>
              <span v-if="application.logoImage">{{ application.logoImageContentType }}, {{ byteSize(application.logoImage) }}</span>
            </td>
            <td>{{ application.createdAt ? $d(Date.parse(application.createdAt), 'short') : '' }}</td>
            <td>{{ application.modifiedAt ? $d(Date.parse(application.modifiedAt), 'short') : '' }}</td>
            <td>
              {{ application.user ? application.user.login : '' }}
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ApplicationView', params: { applicationId: application.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ApplicationEdit', params: { applicationId: application.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(application)"
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
        ><span id="iConnApp.application.delete.question" data-cy="applicationDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-application-heading" v-text="$t('iConnApp.application.delete.question', { id: removeId })">
          Are you sure you want to delete this Application?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-application"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeApplication()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="applications && applications.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./application.component.ts"></script>

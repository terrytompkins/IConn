<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="iConnApp.application.home.createOrEditLabel"
          data-cy="ApplicationCreateUpdateHeading"
          v-text="$t('iConnApp.application.home.createOrEditLabel')"
        >
          Create or edit a Application
        </h2>
        <div>
          <div class="form-group" v-if="application.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="application.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iConnApp.application.name')" for="application-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="application-name"
              data-cy="name"
              :class="{ valid: !$v.application.name.$invalid, invalid: $v.application.name.$invalid }"
              v-model="$v.application.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iConnApp.application.description')" for="application-description"
              >Description</label
            >
            <input
              type="text"
              class="form-control"
              name="description"
              id="application-description"
              data-cy="description"
              :class="{ valid: !$v.application.description.$invalid, invalid: $v.application.description.$invalid }"
              v-model="$v.application.description.$model"
            />
            <div v-if="$v.application.description.$anyDirty && $v.application.description.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.application.description.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 4000 })"
              >
                This field cannot be longer than 4000 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iConnApp.application.wikiUrl')" for="application-wikiUrl">Wiki Url</label>
            <input
              type="text"
              class="form-control"
              name="wikiUrl"
              id="application-wikiUrl"
              data-cy="wikiUrl"
              :class="{ valid: !$v.application.wikiUrl.$invalid, invalid: $v.application.wikiUrl.$invalid }"
              v-model="$v.application.wikiUrl.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iConnApp.application.logoImage')" for="application-logoImage">Logo Image</label>
            <div>
              <img
                v-bind:src="'data:' + application.logoImageContentType + ';base64,' + application.logoImage"
                style="max-height: 100px"
                v-if="application.logoImage"
                alt="application image"
              />
              <div v-if="application.logoImage" class="form-text text-danger clearfix">
                <span class="pull-left">{{ application.logoImageContentType }}, {{ byteSize(application.logoImage) }}</span>
                <button
                  type="button"
                  v-on:click="clearInputImage('logoImage', 'logoImageContentType', 'file_logoImage')"
                  class="btn btn-secondary btn-xs pull-right"
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                </button>
              </div>
              <input
                type="file"
                ref="file_logoImage"
                id="file_logoImage"
                data-cy="logoImage"
                v-on:change="setFileData($event, application, 'logoImage', true)"
                accept="image/*"
                v-text="$t('entity.action.addimage')"
              />
            </div>
            <input
              type="hidden"
              class="form-control"
              name="logoImage"
              id="application-logoImage"
              data-cy="logoImage"
              :class="{ valid: !$v.application.logoImage.$invalid, invalid: $v.application.logoImage.$invalid }"
              v-model="$v.application.logoImage.$model"
            />
            <input
              type="hidden"
              class="form-control"
              name="logoImageContentType"
              id="application-logoImageContentType"
              v-model="application.logoImageContentType"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iConnApp.application.createdAt')" for="application-createdAt">Created At</label>
            <div class="d-flex">
              <input
                id="application-createdAt"
                data-cy="createdAt"
                type="datetime-local"
                class="form-control"
                name="createdAt"
                :class="{ valid: !$v.application.createdAt.$invalid, invalid: $v.application.createdAt.$invalid }"
                required
                :value="convertDateTimeFromServer($v.application.createdAt.$model)"
                @change="updateInstantField('createdAt', $event)"
              />
            </div>
            <div v-if="$v.application.createdAt.$anyDirty && $v.application.createdAt.$invalid">
              <small class="form-text text-danger" v-if="!$v.application.createdAt.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.application.createdAt.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iConnApp.application.modifiedAt')" for="application-modifiedAt"
              >Modified At</label
            >
            <div class="d-flex">
              <input
                id="application-modifiedAt"
                data-cy="modifiedAt"
                type="datetime-local"
                class="form-control"
                name="modifiedAt"
                :class="{ valid: !$v.application.modifiedAt.$invalid, invalid: $v.application.modifiedAt.$invalid }"
                required
                :value="convertDateTimeFromServer($v.application.modifiedAt.$model)"
                @change="updateInstantField('modifiedAt', $event)"
              />
            </div>
            <div v-if="$v.application.modifiedAt.$anyDirty && $v.application.modifiedAt.$invalid">
              <small class="form-text text-danger" v-if="!$v.application.modifiedAt.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.application.modifiedAt.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iConnApp.application.user')" for="application-user">User</label>
            <select class="form-control" id="application-user" data-cy="user" name="user" v-model="application.user" required>
              <option v-if="!application.user" v-bind:value="null" selected></option>
              <option
                v-bind:value="application.user && userOption.id === application.user.id ? application.user : userOption"
                v-for="userOption in users"
                :key="userOption.id"
              >
                {{ userOption.login }}
              </option>
            </select>
          </div>
          <div v-if="$v.application.user.$anyDirty && $v.application.user.$invalid">
            <small class="form-text text-danger" v-if="!$v.application.user.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label v-text="$t('iConnApp.application.tech')" for="application-tech">Tech</label>
            <select
              class="form-control"
              id="application-teches"
              data-cy="tech"
              multiple
              name="tech"
              v-if="application.teches !== undefined"
              v-model="application.teches"
            >
              <option v-bind:value="getSelected(application.teches, techOption)" v-for="techOption in teches" :key="techOption.id">
                {{ techOption.name }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.application.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./application-update.component.ts"></script>

<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="iConnApp.tech.home.createOrEditLabel" data-cy="TechCreateUpdateHeading" v-text="$t('iConnApp.tech.home.createOrEditLabel')">
          Create or edit a Tech
        </h2>
        <div>
          <div class="form-group" v-if="tech.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="tech.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iConnApp.tech.name')" for="tech-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="tech-name"
              data-cy="name"
              :class="{ valid: !$v.tech.name.$invalid, invalid: $v.tech.name.$invalid }"
              v-model="$v.tech.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iConnApp.tech.techType')" for="tech-techType">Tech Type</label>
            <select
              class="form-control"
              name="techType"
              :class="{ valid: !$v.tech.techType.$invalid, invalid: $v.tech.techType.$invalid }"
              v-model="$v.tech.techType.$model"
              id="tech-techType"
              data-cy="techType"
            >
              <option
                v-for="techType in techTypeValues"
                :key="techType"
                v-bind:value="techType"
                v-bind:label="$t('iConnApp.TechType.' + techType)"
              >
                {{ techType }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iConnApp.tech.description')" for="tech-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="tech-description"
              data-cy="description"
              :class="{ valid: !$v.tech.description.$invalid, invalid: $v.tech.description.$invalid }"
              v-model="$v.tech.description.$model"
            />
            <div v-if="$v.tech.description.$anyDirty && $v.tech.description.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.tech.description.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 2000 })"
              >
                This field cannot be longer than 2000 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iConnApp.tech.createdAt')" for="tech-createdAt">Created At</label>
            <div class="d-flex">
              <input
                id="tech-createdAt"
                data-cy="createdAt"
                type="datetime-local"
                class="form-control"
                name="createdAt"
                :class="{ valid: !$v.tech.createdAt.$invalid, invalid: $v.tech.createdAt.$invalid }"
                required
                :value="convertDateTimeFromServer($v.tech.createdAt.$model)"
                @change="updateInstantField('createdAt', $event)"
              />
            </div>
            <div v-if="$v.tech.createdAt.$anyDirty && $v.tech.createdAt.$invalid">
              <small class="form-text text-danger" v-if="!$v.tech.createdAt.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.tech.createdAt.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iConnApp.tech.modifiedAt')" for="tech-modifiedAt">Modified At</label>
            <div class="d-flex">
              <input
                id="tech-modifiedAt"
                data-cy="modifiedAt"
                type="datetime-local"
                class="form-control"
                name="modifiedAt"
                :class="{ valid: !$v.tech.modifiedAt.$invalid, invalid: $v.tech.modifiedAt.$invalid }"
                required
                :value="convertDateTimeFromServer($v.tech.modifiedAt.$model)"
                @change="updateInstantField('modifiedAt', $event)"
              />
            </div>
            <div v-if="$v.tech.modifiedAt.$anyDirty && $v.tech.modifiedAt.$invalid">
              <small class="form-text text-danger" v-if="!$v.tech.modifiedAt.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.tech.modifiedAt.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iConnApp.tech.user')" for="tech-user">User</label>
            <select class="form-control" id="tech-user" data-cy="user" name="user" v-model="tech.user" required>
              <option v-if="!tech.user" v-bind:value="null" selected></option>
              <option
                v-bind:value="tech.user && userOption.id === tech.user.id ? tech.user : userOption"
                v-for="userOption in users"
                :key="userOption.id"
              >
                {{ userOption.login }}
              </option>
            </select>
          </div>
          <div v-if="$v.tech.user.$anyDirty && $v.tech.user.$invalid">
            <small class="form-text text-danger" v-if="!$v.tech.user.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
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
            :disabled="$v.tech.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./tech-update.component.ts"></script>

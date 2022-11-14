<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="application">
        <h2 class="jh-entity-heading" data-cy="applicationDetailsHeading">
          <span v-text="$t('iConnApp.application.detail.title')">Application</span> {{ application.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('iConnApp.application.name')">Name</span>
          </dt>
          <dd>
            <span>{{ application.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('iConnApp.application.description')">Description</span>
          </dt>
          <dd>
            <span>{{ application.description }}</span>
          </dd>
          <dt>
            <span v-text="$t('iConnApp.application.wikiUrl')">Wiki Url</span>
          </dt>
          <dd>
            <span>{{ application.wikiUrl }}</span>
          </dd>
          <dt>
            <span v-text="$t('iConnApp.application.logoImage')">Logo Image</span>
          </dt>
          <dd>
            <div v-if="application.logoImage">
              <a v-on:click="openFile(application.logoImageContentType, application.logoImage)">
                <img
                  v-bind:src="'data:' + application.logoImageContentType + ';base64,' + application.logoImage"
                  style="max-width: 100%"
                  alt="application image"
                />
              </a>
              {{ application.logoImageContentType }}, {{ byteSize(application.logoImage) }}
            </div>
          </dd>
          <dt>
            <span v-text="$t('iConnApp.application.createdAt')">Created At</span>
          </dt>
          <dd>
            <span v-if="application.createdAt">{{ $d(Date.parse(application.createdAt), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('iConnApp.application.modifiedAt')">Modified At</span>
          </dt>
          <dd>
            <span v-if="application.modifiedAt">{{ $d(Date.parse(application.modifiedAt), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('iConnApp.application.user')">User</span>
          </dt>
          <dd>
            {{ application.user ? application.user.login : '' }}
          </dd>
          <dt>
            <span v-text="$t('iConnApp.application.tech')">Tech</span>
          </dt>
          <dd>
            <span v-for="(tech, i) in application.teches" :key="tech.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'TechView', params: { techId: tech.id } }">{{ tech.name }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="application.id"
          :to="{ name: 'ApplicationEdit', params: { applicationId: application.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./application-details.component.ts"></script>

<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="tech">
        <h2 class="jh-entity-heading" data-cy="techDetailsHeading">
          <span v-text="$t('iConnApp.tech.detail.title')">Tech</span> {{ tech.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('iConnApp.tech.name')">Name</span>
          </dt>
          <dd>
            <span>{{ tech.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('iConnApp.tech.techType')">Tech Type</span>
          </dt>
          <dd>
            <span v-text="$t('iConnApp.TechType.' + tech.techType)">{{ tech.techType }}</span>
          </dd>
          <dt>
            <span v-text="$t('iConnApp.tech.description')">Description</span>
          </dt>
          <dd>
            <span>{{ tech.description }}</span>
          </dd>
          <dt>
            <span v-text="$t('iConnApp.tech.createdAt')">Created At</span>
          </dt>
          <dd>
            <span v-if="tech.createdAt">{{ $d(Date.parse(tech.createdAt), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('iConnApp.tech.modifiedAt')">Modified At</span>
          </dt>
          <dd>
            <span v-if="tech.modifiedAt">{{ $d(Date.parse(tech.modifiedAt), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('iConnApp.tech.user')">User</span>
          </dt>
          <dd>
            {{ tech.user ? tech.user.login : '' }}
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="tech.id" :to="{ name: 'TechEdit', params: { techId: tech.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./tech-details.component.ts"></script>

<template>
  <div>
    <b-dropdown-item to="/application">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.application')">Application</span>
    </b-dropdown-item>
    <b-dropdown-item to="/tech">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.tech')">Tech</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>

<template>
  <div
    v-empty="!canShowDetail"
    class="cr-body-content"
    xs-empty-icon="nopermission"
    xs-empty-text="暂无权限">
    <flexbox class="content-header">
      <template v-if="showScene">
        <div>场景：</div>
        <el-select
          v-model="sceneId"
          @change="getList">
          <el-option
            v-for="item in scenesList"
            :key="item.sceneId"
            :label="item.name"
            :value="item.sceneId"/>
        </el-select>
      </template>

      <el-input
        v-if="showSearch"
        v-model="searchContent"
        class="search-container">
        <el-button
          slot="append"
          icon="el-icon-search"
          @click.native="searchInput"/>
      </el-input>
      <el-button
        v-if="canSave"
        class="xr-btn--orange"
        icon="el-icon-plus"
        type="primary"
        @click="isCreate=true">新建</el-button>
    </flexbox>
    <el-table
      v-loading="loading"
      ref="relativeTable"
      :data="list"
      :height="250"
      stripe
      border
      highlight-current-row
      style="width: 100%"
      @select-all="selectAll"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick">
      <el-table-column
        show-overflow-tooltip
        type="selection"
        align="center"
        width="55"/>
      <el-table-column
        v-for="(item, index) in fieldList"
        :key="index"
        :prop="item.field"
        :label="item.name"
        :width="150"
        :formatter="fieldFormatter"
        show-overflow-tooltip/>
      <el-table-column/>
    </el-table>
    <div class="table-footer">
      <el-button
        :disabled="currentPage <= 1"
        @click.native="changePage('up')">上一页</el-button>
      <el-button
        :disabled="currentPage >= totalPage"
        @click.native="changePage('down')">下一页</el-button>
    </div>
    <c-r-m-all-create
      v-if="isCreate"
      :crm-type="crmType"
      @save-success="getList"
      @close="isCreate=false"/>
  </div>
</template>
<script type="text/javascript">
import { mapGetters } from 'vuex'

import crmTypeModel from '@/views/crm/model/crmTypeModel'
import { crmLeadsIndexAPI } from '@/api/crm/leads'
import { crmCustomerIndexAPI } from '@/api/crm/customer'
import {
  crmContactsIndexAPI,
  crmContactsQueryBusinessAPI
} from '@/api/crm/contacts'
import { crmBusinessIndexAPI } from '@/api/crm/business'
import { crmContractIndexAPI } from '@/api/crm/contract'
import { crmProductSaleIndexAPI } from '@/api/crm/product'
import { crmSceneIndexAPI } from '@/api/crm/common'
// 客户下商机和联系人
import {
  crmCustomerQueryBusinessAPI,
  crmCustomerQueryContractAPI,
  crmCustomerQueryContactsAPI
} from '@/api/crm/customer'

export default {
  name: 'CrmRelativeTable', // 相关模块CRMCell
  components: {
    CRMAllCreate: () =>
      import('@/views/crm/components/CRMAllCreate')
  },
  props: {
    show: {
      type: Boolean,
      default: false
    },
    /** 多选框 只能选一个 */
    radio: {
      type: Boolean,
      default: true
    },
    /** 没有值就是全部类型 有值就是当个类型 */
    crmType: {
      type: String,
      default: ''
    },
    /** 已选信息 */
    selectedData: {
      type: Object,
      default: () => {
        return {}
      }
    },
    /**
     * default 默认  condition 固定条件筛选
     * relative: 相关 添加
     */
    action: {
      type: Object,
      default: () => {
        return {
          type: 'default',
          data: {}
        }
      }
    }
  },
  data() {
    return {
      loading: false, // 加载进度
      searchContent: '', // 输入内容
      isCreate: false, // 控制新建
      scenesList: [], // 场景信息
      sceneId: '',

      list: [], // 表数据
      fieldList: [], // 表头数据
      currentPage: 1, // 当前页数
      totalPage: 1, // 总页数

      otherItems: [],
      selectedItem: [] // 勾选的数据 点击确定 传递给父组件
    }
  },
  computed: {
    ...mapGetters(['crm']),

    canSave() {
      if (this.action.hasOwnProperty('showCreate')) {
        return this.action.showCreate
      }
      return this.crm && this.crm[this.crmType] && this.crm[this.crmType].save
    },

    showSearch() {
      if (this.action.hasOwnProperty('showSearch')) {
        return this.action.showSearch
      }
      return true
    },

    // 能否查看详情
    canShowDetail() {
      if (this.action.hasOwnProperty('canShowDetail')) {
        return this.action.canShowDetail
      }
      return this.crm && this.crm[this.crmType] && this.crm[this.crmType].index
    },

    // 展示相关效果 去除场景
    isRelationShow() {
      return this.action.type === 'condition'
    },

    showScene() {
      if (this.action.hasOwnProperty('showScene')) {
        return this.action.showScene
      }
      return !this.isRelationShow && this.crmType != 'product'
    }
  },
  watch: {
    crmType: function(newValue, oldValue) {
      if (newValue != oldValue) {
        this.fieldList = []
        this.getFieldList()
      }
    },
    action: function(val) {
      // if (this.action != val) {
      this.sceneId = ''
      this.list = [] // 表数据
      this.fieldList = [] // 表头数据
      this.currentPage = 1 // 当前页数
      this.totalPage = 1 // 总页数
      if (this.show) {
        if (!this.isRelationShow && this.showScene) {
          this.getSceneList()
        } else {
          this.getFieldList()
        }
      }
      // }
    },
    show: {
      handler(val) {
        if (val && this.fieldList.length == 0) {
          console.log('show', val)
          // 相关列表展示时不需要场景 直接获取展示字段
          if (!this.isRelationShow && this.showScene) {
            this.getSceneList()
          } else {
            this.getFieldList()
          }
        }
      },
      deep: true,
      immediate: true
    },
    // 选择
    selectedData: function() {
      this.checkItemsWithSelectedData()
    }
  },
  mounted() {},
  methods: {
    /**
     * 刷新列表
     */
    refreshList() {
      this.currentPage = 1
      this.getList()
    },

    getSceneList() {
      if (!this.canShowDetail) {
        return
      }
      this.loading = true
      crmSceneIndexAPI({
        type: crmTypeModel[this.crmType]
      })
        .then(res => {
          var defaultScene = res.data.filter(function(item, index) {
            return item.isDefault === 1
          })
          this.scenesList = res.data
          if (defaultScene && defaultScene.length > 0) {
            this.sceneId = defaultScene[0].sceneId
          } else {
            this.sceneId = ''
          }

          this.scenesList.unshift({ sceneId: '', name: '全部' })

          this.getFieldList()
        })
        .catch(() => {
          this.loading = false
        })
    },
    /** 获取字段 */
    getFieldList() {
      if (!this.canShowDetail) {
        return
      }

      if (this.fieldList.length == 0) {
        this.fieldList = this.getDefaultField()
      }
      // 获取好字段开始请求数据
      this.getList()
    },
    /** 获取列表请求 */
    getDefaultField() {
      if (this.crmType === 'leads') {
        return [
          { name: '线索名称', field: 'leadsName', formType: 'leads' },
          { name: '下次联系时间', field: 'nextTime', formType: 'datetime' },
          { name: '最后跟进时间', field: 'updateTime', formType: 'datetime' },
          { name: '创建时间 ', field: 'createTime', formType: 'datetime' }
        ]
      } else if (this.crmType === 'customer') {
        return [
          { name: '客户名称', field: 'customerName', formType: 'customer' },
          { name: '邮箱', field: 'email', formType: 'text' },
          { name: '下次联系时间', field: 'nextTime', formType: 'datetime' },
          { name: '最后跟进时间', field: 'updateTime', formType: 'datetime' },
          { name: '创建时间 ', field: 'createTime', formType: 'datetime' }
        ]
      } else if (this.crmType === 'contacts') {
        return [
          { name: '姓名', field: 'name', formType: 'contacts' },
          { name: '手机', field: 'mobile', formType: 'mobile' },
          { name: '电话', field: 'telephone', formType: 'text' },
          { name: '是否关键决策人', field: 'policymakers', formType: 'text' },
          { name: '职务', field: 'post', formType: 'text' }
        ]
      } else if (this.crmType === 'business') {
        return [
          { name: '商机名称', field: 'businessName', formType: 'text' },
          { name: '商机金额', field: 'money', formType: 'text' },
          { name: '客户名称', field: 'customerName', formType: 'text' },
          { name: '商机状态组 ', field: 'typeName', formType: 'text' },
          { name: '状态 ', field: 'statusName', formType: 'text' }
        ]
      } else if (this.crmType === 'contract') {
        return [
          {
            name: '合同名称',
            field: this.isRelationShow ? 'contractName' : 'name',
            formType: 'text'
          },
          { name: '合同编号', field: 'num', formType: 'text' },
          { name: '客户名称', field: 'customerName', formType: 'text' },
          { name: '合同金额', field: 'money', formType: 'text' },
          { name: '开始日期', field: 'startTime', formType: 'text' },
          { name: '结束日期', field: 'endTime', formType: 'text' }
        ]
      } else if (this.crmType === 'product') {
        return [
          { name: '产品名称', field: 'name', formType: 'text' },
          { name: '单位', field: 'unit', formType: 'text' },
          { name: '价格', field: 'price', formType: 'text' },
          { name: '产品类别', field: 'categoryName', formType: 'text' }
        ]
      } else if (this.crmType === 'invoiceTitle') {
        return [
          { field: 'titleType', formType: 'text', name: '抬头类型' },
          { field: 'invoiceTitle', formType: 'text', name: '开票抬头' },
          { field: 'taxNumber', formType: 'text', name: '纳税人识别号' },
          { field: 'depositBank', formType: 'text', name: '开户行' },
          { field: 'depositAccount', formType: 'text', name: '开户账号' },
          { field: 'depositAddress', formType: 'text', name: '开票地址' },
          { field: 'telephone', formType: 'text', name: '电话' }
        ]
      }
    },
    /** 获取列表数据 */
    getList() {
      this.loading = true
      let crmIndexRequest = this.getIndexRequest()
      let params = { search: this.searchContent }
      // 注入场景
      if (this.sceneId) {
        params.sceneId = this.sceneId
      }
      // 注入关联ID
      if (this.isRelationShow) {
        // this.action.data.moduleType 下的 this.crmType 的列表
        if (this.action.data.moduleType) {
          crmIndexRequest = {
            customer: {
              business: crmCustomerQueryBusinessAPI,
              contacts: crmCustomerQueryContactsAPI,
              contract: crmCustomerQueryContractAPI
            },
            contacts: {
              business: crmContactsQueryBusinessAPI
            }
          }[this.action.data.moduleType][this.crmType]

          params[this.action.data.moduleType + 'Id'] = this.action.data[
            this.action.data.moduleType + 'Id'
          ]
          if (this.action.data.params) {
            for (const field in this.action.data.params) {
              params[field] = this.action.data.params[field]
            }
          }
          // 不分页
          params.pageType = 0
        }
      } else {
        params.page = this.currentPage
        params.limit = 10
        params.type = crmTypeModel[this.crmType]
      }

      if (this.action.request) {
        crmIndexRequest = this.action.request
      }

      if (this.action.params) {
        params = { ...params, ...this.action.params }
      }
      crmIndexRequest(params)
        .then(res => {
          this.list = res.data.list
          /**
           *  如果已选择的有数据
           */

          if (this.selectedData[this.crmType]) {
            this.checkItemsWithSelectedData()
          } else {
            this.list = res.data.list
          }

          if (params.hasOwnProperty('pageType') && params.pageType === 0) {
            this.totalPage = 0
          } else {
            this.totalPage = Math.ceil(res.data.totalRow / 10)
          }
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },
    // 标记选择数据
    checkItemsWithSelectedData() {
      const selectedArray = this.selectedData[this.crmType] ? this.selectedData[this.crmType].map(item => {
        item.has = false
        return item
      }) : []

      const selectedRows = []
      this.otherItems = []

      this.list.forEach((item, index) => {
        selectedArray.forEach((selectedItem, selectedIndex) => {
          if (item[this.crmType + 'Id'] == selectedItem[this.crmType + 'Id']) {
            selectedItem.has = true
            selectedRows.push(item)
          }
        })
      })

      selectedArray.forEach((selectedItem, selectedIndex) => {
        if (!selectedItem.has) {
          this.otherItems.push(selectedItem)
        }
      })

      this.$nextTick(() => {
        this.$refs.relativeTable.clearSelection()
        selectedRows.forEach(row => {
          this.$refs.relativeTable.toggleRowSelection(row, true)
        })
      })
    },
    /** 获取列表请求 */
    getIndexRequest() {
      if (this.crmType === 'leads') {
        return crmLeadsIndexAPI
      } else if (this.crmType === 'customer') {
        return crmCustomerIndexAPI
      } else if (this.crmType === 'contacts') {
        return crmContactsIndexAPI
      } else if (this.crmType === 'business') {
        return crmBusinessIndexAPI
      } else if (this.crmType === 'contract') {
        return crmContractIndexAPI
      } else if (this.crmType === 'product') {
        return crmProductSaleIndexAPI
      }
    },
    fieldFormatter(row, column, cellValue) {
      if (this.crmType === 'invoiceTitle' && column.property == 'titleType') {
        return {
          1: '单位',
          2: '个人'
        }[row[column.property]]
      }
      return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
    },
    // 场景选择
    sceneSelect() {
      this.getList()
    },
    /** 列表操作 */
    // 当某一行被点击时会触发该事件
    handleRowClick(row, column, event) {},
    // 当选择项发生变化时会触发该事件
    handleSelectionChange(val) {
      if (this.radio) {
        // this.$refs.relativeTable.clearSelection();
        val.forEach((row, index) => {
          if (index === val.length - 1) return
          this.$refs.relativeTable.toggleRowSelection(row, false)
        })
        if (val.length === 0) {
          this.selectedItem = []
        } else {
          this.selectedItem = val.length === 1 ? val : [val[val.length - 1]]
        }
      } else {
        this.selectedItem = this.otherItems.concat(val)
      }
      this.$emit('changeCheckout', {
        data: this.selectedItem,
        type: this.crmType
      })
    },
    clearAll() {
      this.$refs.relativeTable.clearSelection()
    },
    // 	当用户手动勾选全选 Checkbox 时触发的事件
    selectAll() {},
    // 进行搜索操作
    searchInput() {
      this.currentPage = 1
      this.totalPage = 1
      this.getList()
    },
    changePage(type) {
      if (type == 'up') {
        this.currentPage = this.currentPage - 1
      } else if (type == 'down') {
        this.currentPage = this.currentPage + 1
      }
      if (this.currentPage <= this.totalPage && this.currentPage >= 1) {
        this.getList()
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.cr-body-content {
  position: relative;
  background-color: white;
  border-bottom: 1px solid $xr-border-line-color;
}

.content-header {
  position: relative;
  padding: 10px 20px;
  color: #333;
  font-size: 13px;
  .el-select {
    width: 120px;

    /deep/ .el-icon-circle-close {
      visibility: hidden;
      display: none;
    }
  }

  .el-date-editor {
    width: 170px;
  }

  .search-container {
    margin: 0 20px;
    width: 200px;
  }

  .xr-btn--orange {
    position: absolute;
    right: 10px;
    top: 15px;
  }
}

//表尾 上一页按钮
.table-footer {
  padding: 8px 20px;
}

.el-table /deep/ thead th {
  font-weight: 400;
  font-size: 12px;
}

.el-table /deep/ tbody tr td {
  font-size: 12px;
}

.el-table /deep/ thead .el-checkbox {
  display: none;
}

body .el-table th.gutter {
  display: table-cell !important;
}

.el-table /deep/ .el-table__body-wrapper {
  height: calc(100% - 48px) !important;
}
.el-table--border {
  border-left: none;
}
</style>

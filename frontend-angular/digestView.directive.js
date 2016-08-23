(function() {
    'use strict';

    angular
        .module('app')
        .directive('djtDigestView', djtDigestView);

    /* @ngInject */
    function djtDigestView() {
        var directive = {
            restrict: 'EA',
            templateUrl: '/digest-view.html',
            link: linkFunc,
            controller: DigestViewCtrl,
            controllerAs: 'vm',
            bindToController: {
              task: '='
            }
        };

        return directive;

        function linkFunc(scope, el, attr, ctrl) {

        }
    }

    DigestViewCtrl.$inject = ['Digest'];

    /* @ngInject */
    function DigestViewCtrl(Digest) {
        var vm = this;

        activate();

        function activate() {
            vm.alertClass = {
               'alert-success': vm.task.status == "SUCCESS",
               'alert-info': vm.task.status == "IN_PROCESS" || vm.task.status == "NEW",
               'alert-warning': vm.task.status == "INTERRUPTED",
               'alert-danger': vm.task.status == "EXCEPTIONAL",
            };
            vm.isDeletable = isDeletable;
            vm.isCancelable = isCancelable;
            vm.remove = remove;
            vm.cancel = cancel;
        }

        function remove(){
            Digest.remove(vm.task.id);
        }

        function cancel(){
            Digest.cancel(vm.task.id);
        }

        function isDeletable(){
            switch(vm.task.status){
                case 'SUCCESS':
                    return true;
                case 'EXCEPTIONAL':
                    return true;
                case 'INTERRUPTED':
                    return true;
            }
            return false;
        }

        function isCancelable(){
            switch(vm.task.status){
                case 'NEW':
                    return true;
                case 'IN_PROCESS':
                    return true;
            }
            return false;
        }
    }
})();

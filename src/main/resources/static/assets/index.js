import{r as V,a as W}from"./react.js";import{r as a,R as z,a as I,S as G,B as R,D as H,b as B,c as J,d as K,M as U,F as S,I as O,e as Y,f as F,P as Q,g as X,U as Z,h as A,s as k}from"./antd.js";import{C as ee,a as te,i as ne,b as se,c as re,f as oe}from"./fullcalendar.js";(function(){const n=document.createElement("link").relList;if(n&&n.supports&&n.supports("modulepreload"))return;for(const r of document.querySelectorAll('link[rel="modulepreload"]'))c(r);new MutationObserver(r=>{for(const o of r)if(o.type==="childList")for(const u of o.addedNodes)u.tagName==="LINK"&&u.rel==="modulepreload"&&c(u)}).observe(document,{childList:!0,subtree:!0});function s(r){const o={};return r.integrity&&(o.integrity=r.integrity),r.referrerPolicy&&(o.referrerPolicy=r.referrerPolicy),r.crossOrigin==="use-credentials"?o.credentials="include":r.crossOrigin==="anonymous"?o.credentials="omit":o.credentials="same-origin",o}function c(r){if(r.ep)return;r.ep=!0;const o=s(r);fetch(r.href,o)}})();var D={exports:{}},v={};/**
 * @license React
 * react-jsx-runtime.production.min.js
 *
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */var _;function ie(){if(_)return v;_=1;var t=V(),n=Symbol.for("react.element"),s=Symbol.for("react.fragment"),c=Object.prototype.hasOwnProperty,r=t.__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED.ReactCurrentOwner,o={key:!0,ref:!0,__self:!0,__source:!0};function u(l,d,y){var f,x={},j=null,g=null;y!==void 0&&(j=""+y),d.key!==void 0&&(j=""+d.key),d.ref!==void 0&&(g=d.ref);for(f in d)c.call(d,f)&&!o.hasOwnProperty(f)&&(x[f]=d[f]);if(l&&l.defaultProps)for(f in d=l.defaultProps,d)x[f]===void 0&&(x[f]=d[f]);return{$$typeof:n,type:l,key:j,ref:g,props:x,_owner:r.current}}return v.Fragment=s,v.jsx=u,v.jsxs=u,v}var P;function ae(){return P||(P=1,D.exports=ie()),D.exports}var e=ae(),E={},M;function ce(){if(M)return E;M=1;var t=W();return E.createRoot=t.createRoot,E.hydrateRoot=t.hydrateRoot,E}var le=ce();const de=parseInt(String(z.version).split(".")[0]),ue=de<18;class q extends a.Component{constructor(){super(...arguments),this.elRef=a.createRef(),this.isUpdating=!1,this.isUnmounting=!1,this.state={customRenderingMap:new Map},this.requestResize=()=>{this.isUnmounting||(this.cancelResize(),this.resizeId=requestAnimationFrame(()=>{this.doResize()}))}}render(){const n=[];for(const s of this.state.customRenderingMap.values())n.push(z.createElement(he,{key:s.id,customRendering:s}));return z.createElement("div",{ref:this.elRef},n)}componentDidMount(){this.isUnmounting=!1;const n=new ee;this.handleCustomRendering=n.handle.bind(n),this.calendar=new te(this.elRef.current,Object.assign(Object.assign({},this.props),{handleCustomRendering:this.handleCustomRendering})),this.calendar.render(),this.calendar.on("_beforeprint",()=>{I.flushSync(()=>{})});let s;n.subscribe(c=>{const r=Date.now(),o=!s;(ue||o||this.isUpdating||this.isUnmounting||r-s<100?$:I.flushSync)(()=>{this.setState({customRenderingMap:c},()=>{s=r,o?this.doResize():this.requestResize()})})})}componentDidUpdate(){this.isUpdating=!0,this.calendar.resetOptions(Object.assign(Object.assign({},this.props),{handleCustomRendering:this.handleCustomRendering})),this.isUpdating=!1}componentWillUnmount(){this.isUnmounting=!0,this.cancelResize(),this.calendar.destroy()}doResize(){this.calendar.updateSize()}cancelResize(){this.resizeId!==void 0&&(cancelAnimationFrame(this.resizeId),this.resizeId=void 0)}getApi(){return this.calendar}}q.act=$;class he extends a.PureComponent{render(){const{customRendering:n}=this.props,{generatorMeta:s}=n,c=typeof s=="function"?s(n.renderProps):s;return I.createPortal(c,n.containerEl)}}function $(t){t()}const C="http://localhost:9000";async function fe(t,n){const{startStr:s,endStr:c}=t,r=new URLSearchParams({startStr:s,endStr:c,teachers:n.join(",")}).toString();return await fetch(`${C}/data?${r}`).then(o=>o.json()).then(o=>{let u=o.data;return u.forEach(l=>{l.title=l.courseName;let d=new Date(Number(l.courseDate));l.start=d.toISOString(),l.end=d.toISOString(),l.allDay=!0}),u}).catch(o=>(console.error("Error:",o),[]))}async function me(){return await fetch(`${C}/teacher`).then(t=>t.json()).then(t=>t.data).catch(t=>console.error("Error:",t))}async function pe(){return await fetch(`${C}/classroom`).then(t=>t.json()).then(t=>t.data).catch(t=>console.error("Error:",t))}async function xe(t){return await fetch(`${C}/classroom`,{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(t)}).then(n=>n.json()).then(n=>n.data).catch(n=>console.error("Error:",n))}async function ge(t){return await fetch(`${C}/classroom/${t}`,{method:"DELETE"}).then(n=>n.json()).then(n=>n.data).catch(n=>console.error("Error:",n))}async function ye(){return await fetch(`${C}/login`).then(t=>t.json()).catch(t=>console.error("Error:",t))}const L=a.createContext(void 0),je=({children:t})=>{const[n,s]=a.useState(window.innerWidth),[c,r]=a.useState(window.innerHeight),o=()=>{s(window.innerWidth),r(window.innerHeight)};return a.useLayoutEffect(()=>(window.addEventListener("resize",o),()=>window.removeEventListener("resize",o)),[]),e.jsx(L.Provider,{value:{width:n,height:c},children:t})},T=()=>a.useContext(L),we=[{label:"month",value:"dayGridMonth"},{label:"week",value:"timeGridWeek"},{label:"day",value:"timeGridDay"}],Re=({calendarApi:t,setTeachers:n})=>{const[s,c]=a.useState("dayGridMonth"),[r,o]=a.useState(!0),[u,l]=a.useState([]),d=T();if(!d)return null;const{width:y}=d;a.useEffect(()=>{(async()=>{const m=(await me()).map(h=>({label:h,value:h}));l(m)})()},[]);const f=p=>{let i=p.toDate();t.gotoDate(i)},x=({target:{value:p}})=>{t.changeView(p),c(p)},j=()=>{o(!r),t.setOption("weekends",!r)},g=p=>{n(p)};return a.useEffect(()=>{t&&t.gotoDate(new Date(2023,2,1))},[t]),e.jsxs(e.Fragment,{children:[e.jsxs("div",{style:{display:"flex",alignItems:"center",padding:"10px 0px"},children:[e.jsx("span",{style:{marginRight:"10px",minWidth:"120px"},children:"Select lecturers:"}),e.jsx(G,{mode:"multiple",allowClear:!0,style:{width:"100%"},placeholder:"Please select lecturers",onChange:g,options:u})]}),e.jsxs("div",{style:{display:"flex",justifyContent:"space-between",alignItems:"center",flexDirection:y>1200?"row":"column"},children:[e.jsxs("div",{style:{display:"flex",alignItems:"center",gap:"10px"},children:[e.jsx(R,{type:"primary",onClick:()=>t==null?void 0:t.today(),children:"Today"}),e.jsx(H,{onChange:f})]}),e.jsxs("div",{style:{display:"flex",alignItems:"center",gap:"10px"},children:[e.jsx(R,{type:"primary",shape:"circle",icon:e.jsx(B,{}),onClick:()=>t==null?void 0:t.prev()}),e.jsx("h1",{style:{minWidth:"300px",textAlign:"center"},children:t==null?void 0:t.view.title}),e.jsx(R,{type:"primary",shape:"circle",icon:e.jsx(J,{}),onClick:()=>t==null?void 0:t.next()})]}),e.jsxs("div",{style:{display:"flex",gap:"10px",margin:"12px 0px"},children:[e.jsx(R,{type:r?"primary":"default",onClick:j,children:"weekends"}),e.jsx(K.Group,{options:we,optionType:"button",buttonStyle:"solid",onChange:x,value:s})]})]})]})},Ce=a.memo(({selectedEvent:t,handleCloseDetails:n})=>{const{title:s,extendedProps:c,start:r}=t||{},{practiceArea:o,courseCode:u,duration:l,software:d,cohort:y,run:f,classroom:x,teacher1:j,teacher2:g,teacher3:p,manager:i,cert:m}=c||{},h=(w,b)=>e.jsxs("p",{children:[e.jsxs("strong",{children:[w,":"]})," ",b]},w);return e.jsxs(U,{width:400,footer:null,title:null,open:t!=null,onOk:n,onCancel:n,children:[e.jsx("h2",{children:s}),h("Practice Area",o),h("Course Code",u),h("Duration (Days)",l),h("Software",d),h("Cohort",y),h("Run #",f),h("Start",r?new Date(r).toLocaleString():""),h("Classroom",x),h("Teacher 1",j),h("Teacher 2",g),h("Teacher 3",p),h("Manager",i),h("Grad Cert (MTech)",m)]})}),Se=()=>{const t=a.useRef(null),[n,s]=a.useState(null),[c,r]=a.useState([]),[o,u]=a.useState([]),[l,d]=a.useState(null);a.useEffect(()=>{var m;let i=(m=t.current)==null?void 0:m.getApi();i&&(s(i),r(i.getEvents()))},[]);const y=T();if(!y)return null;const{width:f}=y,x=i=>{d(i.event)},j=()=>{d(null)},g=i=>{const m=i.event.title,h=i.event.extendedProps,w=ve(m),b=f>1320?"18px":f>660?"14px":"10px",N=f>1320?"14px":f>660?"10px":"8px";return e.jsxs("div",{style:{padding:"4px 8px",backgroundColor:w,cursor:"pointer"},children:[e.jsx("i",{style:{fontSize:b,color:"#111",fontWeight:"bold"},children:m}),e.jsxs("div",{style:{fontSize:N,color:"#333",display:"flex",justifyContent:"space-between"},children:[e.jsx("i",{children:h.classroom}),e.jsx("br",{}),e.jsx("i",{children:h.teacher1})]})]})},p=a.useCallback(async(i,m,h)=>{try{let w=await fe(i,o);r(w),m(w)}catch(w){h(w)}},[o]);return e.jsxs("div",{children:[e.jsx(Re,{calendarApi:n,setTeachers:u}),e.jsx("div",{children:e.jsx(q,{ref:t,plugins:[ne,se,re],headerToolbar:!1,initialView:"dayGridMonth",selectMirror:!0,dayMaxEvents:!0,events:p,eventContent:g,eventClick:x,slotMinTime:"08:00:00",slotMaxTime:"20:00:00",slotDuration:"01:00:00",contentHeight:1500})}),e.jsx(Ce,{selectedEvent:l,handleCloseDetails:j}),e.jsx(Ee,{events:c})]})};function ve(t){let n=0;for(let c=0;c<t.length;c++)n=t.charCodeAt(c)+((n<<5)-n);let s="#";for(let c=0;c<3;c++){const r=n>>c*8&255;s+=("00"+r.toString(16)).substr(-2)}return s}const Ee=({events:t})=>{function n(s){return e.jsxs("li",{children:[e.jsx("b",{children:oe(s.start,{year:"numeric",month:"short",day:"numeric"})}),e.jsx("i",{children:s.title})]},s.id)}return e.jsxs("div",{children:[e.jsxs("h2",{children:["All Events (",t.length,")"]}),e.jsx("ul",{children:t.map(n)})]})},be=a.forwardRef((t,n)=>{const[s]=S.useForm(),[c,r]=a.useState([]),[o,u]=a.useState(!1),[l,d]=a.useState(null);a.useImperativeHandle(n,()=>({add:x}));const y=i=>{d(i),u(!0)},f=async i=>{l&&l.id&&(i.id=l.id),await xe(i),await g(),u(!1),d(null)},x=()=>{d(null),u(!0)},j=async i=>{await ge(i),await g()},g=async()=>{let i=await pe();r(i)};a.useEffect(()=>{g()},[]),a.useEffect(()=>{l?s.setFieldsValue(l):s.resetFields()},[l,s]);const p=[{title:"Classroom",dataIndex:"name",key:"name",sorter:(i,m)=>i.name.localeCompare(m.name)},{title:"Size",dataIndex:"size",key:"size",sorter:(i,m)=>i.size-m.size},{title:"Software",dataIndex:"software",key:"software",render:i=>i||"N/A"},{title:"Action",key:"action",width:100,render:(i,m)=>e.jsxs(F,{size:"middle",children:[e.jsx("a",{onClick:()=>y(m),children:"Edit"}),e.jsx(Q,{title:"Sure to delete?",onConfirm:()=>j(m.id),children:e.jsx("a",{children:"Delete"})})]})}];return e.jsxs(e.Fragment,{children:[e.jsx(U,{title:l?"Edit Classroom":"Create a new classroom",okText:l?"Save":"Create",open:o,onCancel:()=>u(!1),onOk:()=>s.submit(),destroyOnClose:!0,children:e.jsxs(S,{layout:"vertical",form:s,name:"form_in_modal",initialValues:{modifier:"public"},onFinish:f,children:[e.jsx(S.Item,{name:"name",label:"Classroom",rules:[{required:!0,message:"Please input the classroom name!"}],children:e.jsx(O,{})}),e.jsx(S.Item,{name:"size",label:"Size",rules:[{required:!0,message:"Please input the size!"}],children:e.jsx(O,{type:"number"})}),e.jsx(S.Item,{name:"software",label:"Software",children:e.jsx(O,{})})]})}),e.jsx(Y,{columns:p,dataSource:c,rowKey:"id",pagination:{pageSize:10}})]})}),Oe=()=>{const t={accept:".xlsx",action:`${C}/upload`,name:"file",async onChange(n){n.file.status==="done"?k.success(`${n.file.name} file uploaded successfully`):n.file.status==="error"&&k.error(`${n.file.name} file upload failed.`)}};return e.jsx(Z,{...t,children:e.jsx(R,{icon:e.jsx(A,{}),children:"Click to Upload Course"})})},De=()=>{const[t,n]=a.useState(!1),s=a.useRef(null),c=()=>{s.current&&s.current.add()},r=async()=>{await ye()?n(!0):window.location.href="/oauth2/authorization/azure"},o=()=>{n(!1)};return e.jsxs(e.Fragment,{children:[e.jsx(R,{type:"primary",onClick:r,children:"Login & Input Course Data"}),e.jsxs(X,{title:"Input Course Data",onClose:o,open:t,size:"large",children:[e.jsxs(F,{style:{width:"100%",marginBottom:16},children:[e.jsx(Oe,{}),e.jsx(R,{onClick:c,type:"primary",children:"Add a row"})]}),e.jsx(be,{ref:s})]})]})},ze=()=>{const t=T();return t?e.jsxs("div",{style:{padding:t.width>660?"0px 24px":"0"},children:[e.jsx("h1",{children:"AI-based Scheduling System"}),e.jsx(De,{}),e.jsx(Se,{})]}):null};le.createRoot(document.getElementById("root")).render(e.jsx(a.StrictMode,{children:e.jsx(je,{children:e.jsx(ze,{})})}));

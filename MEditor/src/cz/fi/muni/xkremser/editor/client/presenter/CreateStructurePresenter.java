/*
 * Metadata Editor
 * @author Jiri Kremser
 * 
 * 
 * 
 * Metadata Editor - Rich internet application for editing metadata.
 * Copyright (C) 2011  Jiri Kremser (kremser@mzk.cz)
 * Moravian Library in Brno
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * 
 */

package cz.fi.muni.xkremser.editor.client.presenter;

import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Progressbar;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tile.TileGrid;

import cz.fi.muni.xkremser.editor.client.LangConstants;
import cz.fi.muni.xkremser.editor.client.MEditor;
import cz.fi.muni.xkremser.editor.client.NameTokens;
import cz.fi.muni.xkremser.editor.client.dispatcher.DispatchCallback;
import cz.fi.muni.xkremser.editor.client.util.Constants;
import cz.fi.muni.xkremser.editor.client.view.CreateStructureView;
import cz.fi.muni.xkremser.editor.client.view.CreateStructureView.MyUiHandlers;
import cz.fi.muni.xkremser.editor.client.view.other.ScanRecord;
import cz.fi.muni.xkremser.editor.client.view.PageRecord;

import cz.fi.muni.xkremser.editor.shared.event.CreateStructureEvent;
import cz.fi.muni.xkremser.editor.shared.event.CreateStructureEvent.CreateStructureHandler;
import cz.fi.muni.xkremser.editor.shared.event.KeyPressedEvent;
import cz.fi.muni.xkremser.editor.shared.rpc.ImageItem;
import cz.fi.muni.xkremser.editor.shared.rpc.action.ConvertToJPEG2000Action;
import cz.fi.muni.xkremser.editor.shared.rpc.action.ConvertToJPEG2000Result;
import cz.fi.muni.xkremser.editor.shared.rpc.action.ScanFolderAction;
import cz.fi.muni.xkremser.editor.shared.rpc.action.ScanFolderResult;
import cz.fi.muni.xkremser.editor.shared.valueobj.metadata.DublinCore;

// TODO: Auto-generated Javadoc
/**
 * The Class CreateStructurePresenter.
 */
public class CreateStructurePresenter
        extends Presenter<CreateStructurePresenter.MyView, CreateStructurePresenter.MyProxy>
        implements MyUiHandlers, CreateStructureHandler {

    private LangConstants lang;

    private static final Class<CreateStructurePresenter> LOCK = CreateStructurePresenter.class;

    @Inject
    public void setLang(LangConstants lang) {
        this.lang = lang;
    }

    /**
     * The Interface MyView.
     */
    public interface MyView
            extends View, HasUiHandlers<MyUiHandlers> {

        Record[] fromClipboard();

        void toClipboard(final Record[] data);

        PopupPanel getPopupPanel();

        void onAddImages(String model, String code, ScanRecord[] items);

        void escShortCut();
    }

    /**
     * The Interface MyProxy.
     */
    @ProxyCodeSplit
    @NameToken(NameTokens.CREATE)
    public interface MyProxy
            extends ProxyPlace<CreateStructurePresenter> {

    }

    /** The done. */
    private int alreadyDone = 0;

    /** The dispatcher. */
    private final DispatchAsync dispatcher;

    /** The left presenter. */
    private final CreateObjectMenuPresenter leftPresenter;

    /** The doMenuPresenter presenter. */
    private final DigitalObjectMenuPresenter doMenuPresenter;

    /** The code. */
    private String code;

    /** The model. */
    private String model;

    /** The DC. */
    private DublinCore dc;

    /** The place manager. */
    private final PlaceManager placeManager;

    /**
     * Instantiates a new create presenter.
     * 
     * @param eventBus
     *        the event bus
     * @param view
     *        the view
     * @param proxy
     *        the proxy
     * @param leftPresenter
     *        the left presenter
     * @param dispatcher
     *        the dispatcher
     * @param placeManager
     *        the place manager
     */
    @Inject
    public CreateStructurePresenter(final EventBus eventBus,
                                    final MyView view,
                                    final MyProxy proxy,
                                    final CreateObjectMenuPresenter leftPresenter,
                                    final DigitalObjectMenuPresenter doMenuPresenter,
                                    final DispatchAsync dispatcher,
                                    final PlaceManager placeManager) {
        super(eventBus, view, proxy);
        this.leftPresenter = leftPresenter;
        this.doMenuPresenter = doMenuPresenter;
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    /*
     * (non-Javadoc)
     * @see com.gwtplatform.mvp.client.HandlerContainerImpl#onBind()
     */
    @Override
    protected void onBind() {
        super.onBind();
        addRegisteredHandler(KeyPressedEvent.getType(), new KeyPressedEvent.KeyPressedHandler() {

            @Override
            public void onKeyPressed(KeyPressedEvent event) {
                if (event.getCode() == Constants.CODE_KEY_ESC) {
                    getView().escShortCut();
                }
            }

        });
    };

    /*
     * (non-Javadoc)
     * @see
     * com.gwtplatform.mvp.client.Presenter#prepareFromRequest(com.gwtplatform
     * .mvp.client.proxy.PlaceRequest)
     */
    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
    }

    /*
     * (non-Javadoc)
     * @see com.gwtplatform.mvp.client.HandlerContainerImpl#onUnbind()
     */
    @Override
    protected void onUnbind() {
        super.onUnbind();
        // Add unbind functionality here for more complex presenters.
    }

    /*
     * (non-Javadoc)
     * @see com.gwtplatform.mvp.client.PresenterWidget#onReset()
     */
    @Override
    protected void onReset() {
        super.onReset();
        processImages();
        RevealContentEvent.fire(this, AppPresenter.TYPE_SetLeftContent, leftPresenter);
        leftPresenter.getView().setInputTree(doMenuPresenter.getView().getInputTree());
    }

    /**
      * 
      */

    private void processImages() {
        final ScanFolderAction action = new ScanFolderAction(model, code);
        final DispatchCallback<ScanFolderResult> callback = new DispatchCallback<ScanFolderResult>() {

            private volatile int done = 0;
            private int total = 0;
            private volatile boolean isDone = false;

            @Override
            public void callback(ScanFolderResult result) {
                final List<ImageItem> itemList = result.getItems();
                final List<ImageItem> toAdd = result.getToAdd();
                if (toAdd != null && !toAdd.isEmpty()) {
                    int lastItem = toAdd.size();
                    boolean progressbar = lastItem > 5;
                    final Progressbar hBar1 = progressbar ? new Progressbar() : null;
                    if (progressbar) {
                        done = 0;
                        total = lastItem;
                        hBar1.setHeight(24);
                        hBar1.setVertical(false);
                        hBar1.setPercentDone(0);
                        getView().getPopupPanel().setWidget(hBar1);
                        getView().getPopupPanel().setVisible(true);
                        getView().getPopupPanel().center();
                        Timer timer = new Timer() {

                            @Override
                            public void run() {
                                for (ImageItem item : toAdd) {
                                    convertItem(item, hBar1, itemList);
                                }
                            }
                        };
                        timer.schedule(40);
                    } else {
                        for (ImageItem item : toAdd) {
                            convertItem(item, hBar1, itemList);
                        }
                    }
                } else {
                    doTheRest(itemList);
                }
            }

            @Override
            public void callbackError(final Throwable t) {
                if (t.getMessage() != null && t.getMessage().length() > 0
                        && t.getMessage().charAt(0) == Constants.SESSION_EXPIRED_FLAG) {
                    SC.confirm("Session has expired. Do you want to be redirected to login page?",
                               new BooleanCallback() {

                                   @Override
                                   public void execute(Boolean value) {
                                       if (value != null && value) {
                                           MEditor.redirect(t.getMessage().substring(1));
                                       }
                                   }
                               });
                } else {
                    SC.ask(t.getMessage() + " " + lang.mesTryAgain(), new BooleanCallback() {

                        @Override
                        public void execute(Boolean value) {
                            if (value != null && value) {
                                processImages();
                            }
                        }
                    });
                }
                getView().getPopupPanel().setVisible(false);
                getView().getPopupPanel().hide();
            }

            private void doTheRest(List<ImageItem> itemList) {
                ScanRecord[] items = null;
                if (itemList.size() > 0) {
                    items = new ScanRecord[itemList.size()];
                    for (int i = 0, total = itemList.size(); i < total; i++) {
                        items[i] =
                                new ScanRecord(String.valueOf(i), model, code, itemList.get(i)
                                        .getIdentifier(), itemList.get(i).getJpgFsPath());
                    }

                    getView().onAddImages(model, code, items);
                }
                getView().getPopupPanel().setWidget(null);
                getView().getPopupPanel().setVisible(false);
                getView().getPopupPanel().hide();
            }

            private void convertItem(ImageItem item, final Progressbar hBar1, final List<ImageItem> itemList) {
                ConvertToJPEG2000Action action = new ConvertToJPEG2000Action(item);
                final DispatchCallback<ConvertToJPEG2000Result> callback =
                        new DispatchCallback<ConvertToJPEG2000Result>() {

                            @Override
                            public void callback(ConvertToJPEG2000Result result) {
                                done++;
                                if (hBar1 != null) {
                                    hBar1.setPercentDone(((100 * (done + 1)) / total));
                                }
                                if (done >= total && !isDone) {
                                    synchronized (LOCK) {
                                        if (done >= total && !isDone) {
                                            doTheRest(itemList);
                                            isDone = true;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void callbackError(Throwable t) {
                                // unable to convert images to JPEG 2000 install the libraries bitch!
                                if (!isDone) {
                                    synchronized (LOCK) {
                                        if (!isDone) {
                                            doTheRest(itemList);
                                            isDone = true;
                                        }
                                    }
                                }
                            }
                        };
                dispatcher.execute(action, callback);
            }
        };
        Image loader = new Image("images/loadingAnimation3.gif");
        getView().getPopupPanel().setWidget(loader);
        getView().getPopupPanel().setVisible(true);
        getView().getPopupPanel().center();
        dispatcher.execute(action, callback);
    }

    /*
     * (non-Javadoc)
     * @see cz.fi.muni.xkremser.editor.client.view.CreateView.MyUiHandlers#
     * onAddDigitalObject(com.smartgwt.client.widgets.tile.TileGrid,
     * com.smartgwt.client.widgets.menu.Menu)
     */
    @Override
    public void onAddImages(final TileGrid tileGrid, final Menu menu) {
        MenuItem[] items = menu.getItems();
        if (!CreateStructureView.ID_SEL_ALL
                .equals(items[2].getAttributeAsObject(CreateStructureView.ID_NAME))) {
            throw new IllegalStateException("Inconsistent gui.");
        }
        items[2].addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                tileGrid.selectAllRecords();
            }
        });
        if (!CreateStructureView.ID_SEL_NONE.equals(items[3]
                .getAttributeAsObject(CreateStructureView.ID_NAME))) {
            throw new IllegalStateException("Inconsistent gui.");
        }
        items[3].addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                tileGrid.deselectAllRecords();
            }
        });
        if (!CreateStructureView.ID_SEL_INV
                .equals(items[4].getAttributeAsObject(CreateStructureView.ID_NAME))) {
            throw new IllegalStateException("Inconsistent gui.");
        }
        items[4].addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                Record[] selected = tileGrid.getSelection();
                tileGrid.selectAllRecords();
                tileGrid.deselectRecords(selected);
            }
        });
        if (!CreateStructureView.ID_COPY.equals(items[6].getAttributeAsObject(CreateStructureView.ID_NAME))) {
            throw new IllegalStateException("Inconsistent gui.");
        }
        items[6].addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                getView().toClipboard(tileGrid.getSelection());
            }
        });
        if (!CreateStructureView.ID_PASTE.equals(items[7].getAttributeAsObject(CreateStructureView.ID_NAME))) {
            throw new IllegalStateException("Inconsistent gui.");
        }
        items[7].addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                final Record[] data = getView().fromClipboard();
                final boolean progressbar = data.length > Constants.CLIPBOARD_MAX_WITHOUT_PROGRESSBAR;
                final Progressbar hBar1 = progressbar ? new Progressbar() : null;
                if (progressbar) {
                    hBar1.setHeight(24);
                    hBar1.setVertical(false);
                    hBar1.setPercentDone(0);
                    getView().getPopupPanel().setWidget(hBar1);
                    getView().getPopupPanel().setVisible(true);
                    getView().getPopupPanel().center();
                    alreadyDone = 0;
                    Timer timer = new Timer() {

                        @Override
                        public void run() {
                            hBar1.setPercentDone(((100 * (alreadyDone + 1)) / data.length));
                            tileGrid.addData(((PageRecord) data[alreadyDone]).deepCopy());
                            if (++alreadyDone != data.length) {
                                schedule(15);
                            } else {
                                getView().getPopupPanel().setVisible(false);
                                getView().getPopupPanel().hide();
                            }
                        }
                    };
                    timer.schedule(40);
                } else {
                    for (int i = 0; i < data.length; i++) {
                        tileGrid.addData(((PageRecord) data[i]).deepCopy());
                    }
                }
            }
        });
        if (!CreateStructureView.ID_DELETE.equals(items[8].getAttributeAsObject(CreateStructureView.ID_NAME))) {
            throw new IllegalStateException("Inconsistent gui.");
        }

        items[8].addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                tileGrid.removeSelectedData();
            }
        });
    }

    /*
     * (non-Javadoc)
     * @see com.gwtplatform.mvp.client.Presenter#revealInParent()
     */
    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, AppPresenter.TYPE_SetMainContent, this);
    }

    @ProxyEvent
    @Override
    public void onCreateStructure(CreateStructureEvent event) {
        this.model = event.getModel();
        this.code = event.getCode();
        this.dc = event.getDc();
        //        forceReveal();
        //        leftPresenter.getView().setInputTree(doMenuPresenter.getView().getInputTree());
        //        leftPresenter.getView().setInputTree(event.getTree());
    }
}